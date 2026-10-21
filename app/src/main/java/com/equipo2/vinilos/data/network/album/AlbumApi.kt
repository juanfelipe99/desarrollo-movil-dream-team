package com.equipo2.vinilos.data.network.album

import retrofit2.http.Body
import retrofit2.http.POST

interface AlbumApi {

    @POST("albums")
    suspend fun createAlbum(@Body request: CreateAlbumRequest): AlbumDto
}
