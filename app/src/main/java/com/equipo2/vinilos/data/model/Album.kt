package com.equipo2.vinilos.data.model

import java.time.LocalDate

data class Track(
    val id: Int,
    val name: String,
    val duration: String,
)

data class AlbumComment(
    val id: Int,
    val description: String,
    val rating: Int,
)

data class Performer(
    val id: Int,
    val name: String,
    val image: String,
    val description: String,
    val birthDate: String,
)

data class Album(
    val id: Int,
    val name: String,
    val cover: String,
    val releaseDate: LocalDate,
    val description: String,
    val genre: String,
    val recordLabel: String,
    val tracks: List<Track> = emptyList(),
    val performers: List<Performer> = emptyList(),
    val comments: List<AlbumComment> = emptyList(),
)

/** Datos para crear un álbum, antes de que la API le asigne un id. */
data class NewAlbum(
    val name: String,
    val cover: String,
    val releaseDate: LocalDate,
    val description: String,
    val genre: String,
    val recordLabel: String,
)

/** Únicos valores aceptados, cualquier otro responde 400. */
object AlbumCatalog {
    val genres = listOf("Classical", "Salsa", "Rock", "Folk")
    val recordLabels = listOf("Sony Music", "EMI", "Discos Fuentes", "Elektra", "Fania Records")
}
