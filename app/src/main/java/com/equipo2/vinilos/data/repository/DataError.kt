package com.equipo2.vinilos.data.repository

import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.time.format.DateTimeParseException

/** Fallo de la capa de datos ya traducido, con un mensaje que se puede mostrar al usuario. */
sealed class DataError(message: String, cause: Throwable?) : Exception(message, cause) {

    class Network(cause: Throwable? = null) :
        DataError("No hay conexión con el servidor. Revisa tu internet e inténtalo de nuevo.", cause)

    class Timeout(cause: Throwable? = null) :
        DataError("El servidor tardó demasiado en responder. Inténtalo de nuevo.", cause)

    class NotFound(cause: Throwable? = null) :
        DataError("No encontramos lo que buscas.", cause)

    class Request(val code: Int, cause: Throwable? = null) :
        DataError("El servidor rechazó la solicitud (código $code).", cause)

    class Server(val code: Int, cause: Throwable? = null) :
        DataError("El servidor tuvo un problema (código $code). Inténtalo más tarde.", cause)

    class InvalidData(cause: Throwable? = null) :
        DataError("El servidor respondió con datos que no pudimos leer.", cause)

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
