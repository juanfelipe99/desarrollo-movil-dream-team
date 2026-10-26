package com.equipo2.vinilos.ui.state

import com.equipo2.vinilos.data.repository.DataError
import com.equipo2.vinilos.data.repository.toDataError

/** Estado de una pantalla que carga datos. */
sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>

    data class Error(val reason: ErrorReason) : UiState<Nothing>

    data object Empty : UiState<Nothing>

    data class Success<T>(val data: T) : UiState<T>
}

/** Por qué falló la carga, que decide qué le pide la pantalla al usuario. */
enum class ErrorReason {
    /** Sin conexión o sin respuesta a tiempo. */
    CONNECTION,

    /** El servidor respondió con un error o con datos que la app no entiende. */
    SERVER,
}

fun Throwable.toErrorReason(): ErrorReason = when (toDataError()) {
    is DataError.Network, is DataError.Timeout -> ErrorReason.CONNECTION
    else -> ErrorReason.SERVER
}
