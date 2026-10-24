package com.equipo2.vinilos.ui.albums.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.equipo2.vinilos.VinilosApplication
import com.equipo2.vinilos.data.model.Album
import com.equipo2.vinilos.data.repository.AlbumRepository
import com.equipo2.vinilos.ui.state.UiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/** Catálogo de álbumes (HU01): carga, éxito, vacío o error, y se conserva al girar. */
class AlbumListViewModel(private val repository: AlbumRepository) : ViewModel() {

    private val currentState = MutableStateFlow<UiState<List<Album>>>(UiState.Loading)

    val state: StateFlow<UiState<List<Album>>> = currentState

    private var loading: Job? = null

    init {
        load()
    }

    /** Vuelve a pedir el catálogo, por ejemplo desde el botón «Reintentar». */
    fun retry() = load()

    private fun load() {
        // Si ya hay una petición en curso no se lanza otra
        if (loading?.isActive == true) return

        currentState.value = UiState.Loading
        loading = viewModelScope.launch {
            currentState.value = repository.getAlbums().fold(
                onSuccess = { albums -> if (albums.isEmpty()) UiState.Empty else UiState.Success(albums) },
                onFailure = { UiState.Error },
            )
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer { AlbumListViewModel(VinilosApplication.container.albumRepository) }
        }
    }
}
