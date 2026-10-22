package com.equipo2.vinilos.ui.common

import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val shortDateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")

/** Fecha como la muestra el prototipo, por ejemplo «01/06/1992». */
fun LocalDate.toShortText(): String = format(shortDateFormat)
