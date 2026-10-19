package com.equipo2.vinilos.data.repository

import com.equipo2.vinilos.data.model.Album
import com.equipo2.vinilos.data.model.NewAlbum
import com.equipo2.vinilos.data.network.NetworkServiceAdapter

import kotlin.coroutines.cancellation.CancellationException

interface AlbumRepository {
    suspend fun createAlbum(album: NewAlbum): Result<Album>
}

class AlbumRepositoryImpl(private val adapter: NetworkServiceAdapter) : AlbumRepository {

    override suspend fun createAlbum(album: NewAlbum): Result<Album> = resultOf { adapter.createAlbum(album) }
}

/** Deja pasar la cancelación de la corrutina en lugar de tratarla como un error. */
internal suspend fun <T> resultOf(block: suspend () -> T): Result<T> = try {
    Result.success(block())
} catch (e: CancellationException) {
    throw e
} catch (e: Exception) {
    Result.failure(e)
}
