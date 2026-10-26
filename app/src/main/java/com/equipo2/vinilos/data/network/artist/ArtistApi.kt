package com.equipo2.vinilos.data.network.artist

import retrofit2.http.GET

interface ArtistApi {

    @GET("musicians")
    suspend fun getMusicians(): List<MusicianDto>

    @GET("bands")
    suspend fun getBands(): List<BandDto>
}
