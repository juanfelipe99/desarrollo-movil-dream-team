package com.equipo2.vinilos.data.network.album

import com.equipo2.vinilos.data.model.Album
import com.equipo2.vinilos.data.model.NewAlbum

/** Llamadas de red del álbum. Oculta Retrofit y el JSON, y entrega modelos de la app. */
class AlbumServiceAdapter(private val api: AlbumApi) {

    suspend fun createAlbum(album: NewAlbum): Album = api.createAlbum(album.toRequest()).toAlbum()
}
