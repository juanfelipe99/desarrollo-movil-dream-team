package com.equipo2.vinilos.data.repository

import kotlin.coroutines.cancellation.CancellationException

// Convierte los errores en Result para que los ViewModels no tengan que atraparlos
internal suspend fun <T> resultOf(block: suspend () -> T): Result<T> = try {
    Result.success(block())
} catch (e: CancellationException) {
    throw e
} catch (e: Exception) {
    Result.failure(e)
}
