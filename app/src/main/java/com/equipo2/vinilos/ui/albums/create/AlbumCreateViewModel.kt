package com.equipo2.vinilos.ui.albums.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.equipo2.vinilos.VinilosApplication
import com.equipo2.vinilos.data.repository.AlbumRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** Estado del formulario de Crear álbum, sus errores y el guardado, que se conservan al girar la pantalla. */
class AlbumCreateViewModel(private val repository: AlbumRepository) : ViewModel() {

    private val currentForm = MutableStateFlow(AlbumForm())
    private val currentErrors = MutableStateFlow<Map<AlbumField, Int>>(emptyMap())
    private val currentSaving = MutableStateFlow(false)

    val form: StateFlow<AlbumForm> = currentForm
    val errors: StateFlow<Map<AlbumField, Int>> = currentErrors
    val isSaving: StateFlow<Boolean> = currentSaving

    // Los errores aparecen al tocar Guardar, y desde ahí se actualizan con cada cambio
    private var validateOnChange = false

    /** Aplica un cambio del formulario. */
    fun update(change: AlbumForm.() -> AlbumForm) {
        currentForm.update(change)
        if (validateOnChange) currentErrors.value = currentForm.value.errors()
    }

    /** Envía el álbum si el formulario es válido, y si no muestra los errores y devuelve false. */
    fun save(): Boolean {
        validateOnChange = true
        currentErrors.value = currentForm.value.errors()
        if (currentErrors.value.isNotEmpty()) return false

        currentSaving.value = true
        viewModelScope.launch {
            repository.createAlbum(currentForm.value.toNewAlbum())
            currentSaving.value = false
        }
        return true
    }

    companion object {
        /** Crea el ViewModel con el repositorio de AppContainer. */
        val Factory = viewModelFactory {
            initializer { AlbumCreateViewModel(VinilosApplication.container.albumRepository) }
        }
    }
}
