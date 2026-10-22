package com.equipo2.vinilos.data.model

import java.time.LocalDate

data class Album(
    val id: Int,
    val name: String,
    val cover: String,
    val releaseDate: LocalDate,
    val description: String,
    val genre: String,
    val recordLabel: String,
)

data class NewAlbum(
    val name: String,
    val cover: String,
    val releaseDate: LocalDate,
    val description: String,
    val genre: String,
    val recordLabel: String,
)

// Únicos géneros y sellos que acepta la API, cualquier otro responde 400
object AlbumCatalog {
    val genres = listOf("Classical", "Salsa", "Rock", "Folk")
    val recordLabels = listOf("Sony Music", "EMI", "Discos Fuentes", "Elektra", "Fania Records")
}
