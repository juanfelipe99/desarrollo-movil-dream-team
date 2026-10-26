package com.equipo2.vinilos.data.repository

import kotlin.coroutines.cancellation.CancellationException

// Los ViewModels reciben un Result con un DataError en lugar de atrapar excepciones
internal suspend fun <T> resultOf(block: suspend () -> T): Result<T> = try {
    Result.success(block())
} catch (e: CancellationException) {
    throw e
} catch (e: Exception) {
    Result.failure(e.toDataError())
}
