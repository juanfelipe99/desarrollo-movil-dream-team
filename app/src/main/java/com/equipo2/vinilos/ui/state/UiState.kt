package com.equipo2.vinilos.ui.state

/** Estado de una pantalla que carga datos. */
sealed interface UiState<out T> {
    /** Esperando la respuesta, se muestra con showLoading. */
    data object Loading : UiState<Nothing>

    /** Falló la carga, se muestra con showError. */
    data object Error : UiState<Nothing>

    /** Cargó pero no hay nada, se muestra con showEmpty. */
    data object Empty : UiState<Nothing>

    /** Cargó con datos y la pantalla muestra su contenido. */
    data class Success<T>(val data: T) : UiState<T>
}
