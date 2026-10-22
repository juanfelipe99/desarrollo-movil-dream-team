package com.equipo2.vinilos.ui.albums.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.equipo2.vinilos.VinilosApplication
import com.equipo2.vinilos.data.repository.AlbumRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** Resultado de enviar el álbum a la API. */
sealed interface AlbumCreateEvent {
    /** La API creó el álbum con este id. */
    data class Saved(val albumId: Int) : AlbumCreateEvent

    /** No se pudo crear, y el formulario conserva lo escrito. */
    data object Failed : AlbumCreateEvent
}

/** Estado del formulario de Crear álbum, sus errores y el guardado, que se conservan al girar la pantalla. */
class AlbumCreateViewModel(private val repository: AlbumRepository) : ViewModel() {

    private val currentForm = MutableStateFlow(AlbumForm())
    private val currentErrors = MutableStateFlow<Map<AlbumField, Int>>(emptyMap())
    private val currentSaving = MutableStateFlow(false)
    private val pendingEvents = Channel<AlbumCreateEvent>(Channel.BUFFERED)

    val form: StateFlow<AlbumForm> = currentForm
    val errors: StateFlow<Map<AlbumField, Int>> = currentErrors
    val isSaving: StateFlow<Boolean> = currentSaving

    /** Resultado de cada guardado, que la pantalla recibe una sola vez aunque se gire. */
    val events: Flow<AlbumCreateEvent> = pendingEvents.receiveAsFlow()

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
            val result = repository.createAlbum(currentForm.value.toNewAlbum())
            currentSaving.value = false
            pendingEvents.send(result.fold({ AlbumCreateEvent.Saved(it.id) }, { AlbumCreateEvent.Failed }))
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
