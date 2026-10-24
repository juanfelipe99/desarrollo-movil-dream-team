package com.equipo2.vinilos.data.repository

import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.time.format.DateTimeParseException

/** Fallo de la capa de datos ya traducido, con un mensaje que se puede mostrar al usuario. */
sealed class DataError(message: String, cause: Throwable?) : Exception(message, cause) {

    /** Sin internet o el servidor no está disponible. */
    class Network(cause: Throwable? = null) :
        DataError("No hay conexión con el servidor. Revisa tu internet e inténtalo de nuevo.", cause)

    /** El servidor no respondió a tiempo. */
    class Timeout(cause: Throwable? = null) :
        DataError("El servidor tardó demasiado en responder. Inténtalo de nuevo.", cause)

    /** El recurso pedido no existe (404). */
    class NotFound(cause: Throwable? = null) :
        DataError("No encontramos lo que buscas.", cause)

    /** El servidor rechazó la solicitud (4xx), por ejemplo por datos inválidos. */
    class Request(val code: Int, cause: Throwable? = null) :
        DataError("El servidor rechazó la solicitud (código $code).", cause)

    /** El servidor falló al procesar la solicitud (5xx). */
    class Server(val code: Int, cause: Throwable? = null) :
        DataError("El servidor tuvo un problema (código $code). Inténtalo más tarde.", cause)

    /** La respuesta llegó, pero con un formato que la app no entiende. */
    class InvalidData(cause: Throwable? = null) :
        DataError("El servidor respondió con datos que no pudimos leer.", cause)

    /** Cualquier otro error. */
    class Unknown(cause: Throwable? = null) :
        DataError("Ocurrió un error inesperado. Inténtalo de nuevo.", cause)
}

/** Traduce las excepciones de Retrofit, OkHttp y la deserialización a un [DataError]. */
fun Throwable.toDataError(): DataError = when (this) {
    is DataError -> this
    // SocketTimeoutException también es IOException, por eso va primero
    is SocketTimeoutException -> DataError.Timeout(this)
    is IOException -> DataError.Network(this)
    is HttpException -> when (val code = code()) {
        404 -> DataError.NotFound(this)
        in 400..499 -> DataError.Request(code, this)
        in 500..599 -> DataError.Server(code, this)
        else -> DataError.Unknown(this)
    }
    is SerializationException, is DateTimeParseException -> DataError.InvalidData(this)
    else -> DataError.Unknown(this)
}
