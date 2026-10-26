package com.equipo2.vinilos.data.repository

import kotlinx.serialization.SerializationException
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.time.LocalDate

class DataErrorTest {

    private fun httpError(code: Int) = HttpException(Response.error<Any>(code, "".toResponseBody()))

    @Test
    fun sinConexion_esErrorDeRed() {
        assertTrue(UnknownHostException("api").toDataError() is DataError.Network)
        assertTrue(IOException("reset").toDataError() is DataError.Network)
    }

    @Test
    fun tiempoAgotado_esTimeout() {
        assertTrue(SocketTimeoutException().toDataError() is DataError.Timeout)
    }

    @Test
    fun error404_esNoEncontrado() {
        assertTrue(httpError(404).toDataError() is DataError.NotFound)
    }

    @Test
    fun error4xx_esSolicitudRechazada() {
        val error = httpError(400).toDataError()

        assertTrue(error is DataError.Request)
        assertEquals(400, (error as DataError.Request).code)
    }

    @Test
    fun error5xx_esErrorDelServidor() {
        val error = httpError(503).toDataError()

        assertTrue(error is DataError.Server)
        assertEquals(503, (error as DataError.Server).code)
    }

    @Test
    fun jsonOFechaInvalidos_sonDatosInvalidos() {
        assertTrue(SerializationException("campo faltante").toDataError() is DataError.InvalidData)

        val badDate = runCatching { LocalDate.parse("no-es-fecha") }.exceptionOrNull()!!
        assertTrue(badDate.toDataError() is DataError.InvalidData)
    }

    @Test
    fun otroError_esDesconocido() {
        assertTrue(IllegalStateException().toDataError() is DataError.Unknown)
    }

    @Test
    fun unDataError_seConservaIgual() {
        val original = DataError.NotFound()

        assertSame(original, original.toDataError())
    }

    @Test
    fun cadaError_guardaLaCausaYTieneMensaje() {
        val cause = IOException("reset")
        val error = cause.toDataError()

        assertSame(cause, error.cause)
        assertTrue(error.message!!.isNotBlank())
    }
}
