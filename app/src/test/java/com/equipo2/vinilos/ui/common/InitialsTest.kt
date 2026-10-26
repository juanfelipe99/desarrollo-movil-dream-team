package com.equipo2.vinilos.ui.common

import org.junit.Assert.assertEquals
import org.junit.Test

class InitialsTest {

    @Test
    fun usaLaPrimeraLetraDeLasDosPrimerasPalabras() {
        assertEquals("RB", initialsOf("Rubén Blades Bellido de Luna"))
    }

    @Test
    fun conUnaSolaPalabra_usaUnaLetra() {
        assertEquals("Q", initialsOf("Queen"))
    }

    @Test
    fun ignoraEspaciosDeMas_yPasaAMayusculas() {
        assertEquals("SD", initialsOf("  seis   del Solar"))
    }
}
