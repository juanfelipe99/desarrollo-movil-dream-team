package com.equipo2.vinilos.ui.common

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class DatesTest {

    @Test
    fun fechaCorta_conDiaYMesDeDosCifras() {
        assertEquals("01/06/1992", LocalDate.of(1992, 6, 1).toShortText())
        assertEquals("13/09/2000", LocalDate.of(2000, 9, 13).toShortText())
    }
}
