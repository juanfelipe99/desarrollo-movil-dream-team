package com.equipo2.vinilos.data.repository

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

class ResultOfTest {

    @Test
    fun siTodoSaleBien_devuelveElValor() = runTest {
        assertEquals(Result.success(42), resultOf { 42 })
    }

    @Test
    fun siHayUnError_loDevuelveComoFallo() = runTest {
        val result = resultOf { throw IOException("Sin conexión") }

        assertTrue(result.exceptionOrNull() is IOException)
    }

    @Test(expected = CancellationException::class)
    fun siSeCancela_noLoConvierteEnFallo() = runTest {
        resultOf { throw CancellationException("Pantalla cerrada") }
    }
}
