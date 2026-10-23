package com.equipo2.vinilos.ui.albums

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.equipo2.vinilos.VinilosApplication
import com.equipo2.vinilos.data.model.Album
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AlbumDetailViewModel(
    private val albumId: Int,
) : ViewModel() {

    private val _album = MutableStateFlow<Album?>(null)
    val album: StateFlow<Album?> = _album.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadAlbum() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            VinilosApplication.container.albumRepository
                .getAlbumById(albumId)
                .onSuccess { result ->
                    _album.value = result
                }
                .onFailure { exception ->
                    _error.value = exception.message ?: "No se pudo cargar el álbum."
                }

            _isLoading.value = false
        }
    }

    companion object {
        fun factory(albumId: Int) = viewModelFactory {
            initializer {
                AlbumDetailViewModel(albumId)
            }
        }
    }
}
