package com.equipo2.vinilos.data.network.album

import com.equipo2.vinilos.data.model.Album
import com.equipo2.vinilos.data.model.NewAlbum
import com.equipo2.vinilos.data.network.fromApiDate
import com.equipo2.vinilos.data.network.toApiDate
import kotlinx.serialization.Serializable

@Serializable
data class AlbumDto(
    val id: Int,
    val name: String,
    val cover: String,
    val releaseDate: String,
    val description: String,
    val genre: String,
    val recordLabel: String,
)

@Serializable
data class CreateAlbumRequest(
    val name: String,
    val cover: String,
    val releaseDate: String,
    val description: String,
    val genre: String,
    val recordLabel: String,
)

internal fun NewAlbum.toRequest() = CreateAlbumRequest(
    name = name,
    cover = cover,
    releaseDate = toApiDate(releaseDate),
    description = description,
    genre = genre,
    recordLabel = recordLabel,
)

internal fun AlbumDto.toAlbum() = Album(
    id = id,
    name = name,
    cover = cover,
    releaseDate = fromApiDate(releaseDate),
    description = description,
    genre = genre,
    recordLabel = recordLabel,
)
