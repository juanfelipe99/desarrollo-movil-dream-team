package com.equipo2.vinilos.data.repository

import kotlin.coroutines.cancellation.CancellationException

/** Deja pasar la cancelación de la corrutina en lugar de tratarla como un error. */
internal suspend fun <T> resultOf(block: suspend () -> T): Result<T> = try {
    Result.success(block())
} catch (e: CancellationException) {
    throw e
} catch (e: Exception) {
    Result.failure(e)
}
