package com.equipo2.vinilos.ui.common

/** Estado de una pantalla que carga datos. */
sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data object Empty : UiState<Nothing>
    data object Error : UiState<Nothing>
}
