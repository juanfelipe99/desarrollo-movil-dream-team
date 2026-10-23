package com.equipo2.vinilos.ui.common

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private val spanish = Locale.forLanguageTag("es")
private val shortDateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")
private val longDateFormat = DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy", spanish)

/** Fecha como la muestra el prototipo, por ejemplo «01/06/1992». */
fun LocalDate.toShortText(): String = format(shortDateFormat)

/** Fecha larga, por ejemplo «1 de agosto de 1984». */
fun LocalDate.toLongText(): String =
    // En Android 5.0 el mes sale con mayúscula inicial
    format(longDateFormat).lowercase(spanish)
