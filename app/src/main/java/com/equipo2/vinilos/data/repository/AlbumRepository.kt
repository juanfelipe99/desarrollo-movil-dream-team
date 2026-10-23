package com.equipo2.vinilos.data.repository

import com.equipo2.vinilos.data.model.Album
import com.equipo2.vinilos.data.model.NewAlbum
import com.equipo2.vinilos.data.network.album.AlbumServiceAdapter

interface AlbumRepository {
    suspend fun createAlbum(album: NewAlbum): Result<Album>
    suspend fun getAlbumById(id: Int): Result<Album>
}

class AlbumRepositoryImpl(private val adapter: AlbumServiceAdapter) : AlbumRepository {

    override suspend fun createAlbum(album: NewAlbum): Result<Album> = resultOf { adapter.createAlbum(album) }

    override suspend fun getAlbumById(id: Int): Result<Album> = resultOf { adapter.getAlbumById(id) }
}
