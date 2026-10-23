package com.equipo2.vinilos.data.network

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class ApiDatesTest {

    @Test
    fun toApiDate_usaElFormatoDeLaApi() {
        assertEquals("1984-08-01T00:00:00.000Z", toApiDate(LocalDate.of(1984, 8, 1)))
    }

    @Test
    fun fromApiDate_guardaSoloElDia() {
        assertEquals(LocalDate.of(1984, 8, 1), fromApiDate("1984-08-01T00:00:00.000Z"))
    }

    @Test
    fun idaYVuelta_conservaLaFecha() {
        val date = LocalDate.of(1992, 6, 1)

        assertEquals(date, fromApiDate(toApiDate(date)))
    }
}
