package com.equipo2.vinilos.data.network.album

import com.equipo2.vinilos.data.model.Album
import com.equipo2.vinilos.data.model.NewAlbum
import com.equipo2.vinilos.data.network.ApiClient

class AlbumServiceAdapter(apiClient: ApiClient) {

    private val api = apiClient.create(AlbumApi::class.java)

    suspend fun createAlbum(album: NewAlbum): Album = api.createAlbum(album.toRequest()).toAlbum()
}
