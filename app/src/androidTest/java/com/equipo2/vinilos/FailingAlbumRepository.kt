package com.equipo2.vinilos

import com.equipo2.vinilos.data.model.Album
import com.equipo2.vinilos.data.model.NewAlbum
import com.equipo2.vinilos.data.repository.AlbumRepository
import java.io.IOException

/** Repositorio que siempre falla al crear, porque en la API compartida del equipo no se puede forzar un error. */
class FailingAlbumRepository : AlbumRepository {

    override suspend fun createAlbum(album: NewAlbum): Result<Album> = Result.failure(IOException("Sin conexión"))
}
