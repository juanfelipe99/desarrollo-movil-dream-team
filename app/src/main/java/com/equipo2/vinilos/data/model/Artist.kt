package com.equipo2.vinilos.data.model

import java.time.LocalDate

enum class ArtistType { MUSICIAN, BAND }

/** Músico o banda. La fecha es el nacimiento del músico o la creación de la banda. */
data class Artist(
    val id: Int,
    val name: String,
    val image: String,
    val type: ArtistType,
    val date: LocalDate,
)
