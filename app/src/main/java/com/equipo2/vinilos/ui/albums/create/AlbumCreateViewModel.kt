package com.equipo2.vinilos.ui.albums.create

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

/** Estado del formulario de Crear álbum y sus errores, que se conservan al girar la pantalla. */
class AlbumCreateViewModel : ViewModel() {

    private val currentForm = MutableStateFlow(AlbumForm())
    private val currentErrors = MutableStateFlow<Map<AlbumField, Int>>(emptyMap())

    val form: StateFlow<AlbumForm> = currentForm
    val errors: StateFlow<Map<AlbumField, Int>> = currentErrors

    // Los errores aparecen al tocar Guardar, y desde ahí se actualizan con cada cambio
    private var validateOnChange = false

    /** Aplica un cambio del formulario. */
    fun update(change: AlbumForm.() -> AlbumForm) {
        currentForm.update(change)
        if (validateOnChange) currentErrors.value = currentForm.value.errors()
    }

    /** Muestra los errores del formulario y devuelve si se puede guardar. */
    fun validate(): Boolean {
        validateOnChange = true
        currentErrors.value = currentForm.value.errors()
        return currentErrors.value.isEmpty()
    }
}
