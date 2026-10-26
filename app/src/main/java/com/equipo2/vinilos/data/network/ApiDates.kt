package com.equipo2.vinilos.data.network

import java.time.LocalDate
import java.time.OffsetDateTime

// La API usa fechas como «1984-08-01T00:00:00.000Z» y la app solo guarda el día
fun toApiDate(date: LocalDate): String = "${date}T00:00:00.000Z"

fun fromApiDate(value: String): LocalDate = OffsetDateTime.parse(value).toLocalDate()
