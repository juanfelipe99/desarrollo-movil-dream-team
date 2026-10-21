package com.equipo2.vinilos.ui.state

/** Estado de una pantalla que carga datos. */
sealed interface UiState<out T> {
    /** Esperando la respuesta. Se muestra con showLoading. */
    data object Loading : UiState<Nothing>

    /** Falló la carga. Se muestra con showError. */
    data object Error : UiState<Nothing>

    /** Cargó, pero no hay nada. Se muestra con showEmpty. */
    data object Empty : UiState<Nothing>

    /** Cargó con datos. La pantalla muestra su contenido. */
    data class Success<T>(val data: T) : UiState<T>
}
