package com.equipo2.vinilos.data.network.artist

import com.equipo2.vinilos.data.model.Artist
import com.equipo2.vinilos.data.network.ApiClient
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class ArtistServiceAdapter(apiClient: ApiClient) {

    private val api = apiClient.create(ArtistApi::class.java)

    // Músicos y bandas se piden a la vez, y si falla cualquiera de los dos falla el listado
    suspend fun getArtists(): List<Artist> = coroutineScope {
        val musicians = async { api.getMusicians() }
        val bands = async { api.getBands() }
        musicians.await().map { it.toArtist() } + bands.await().map { it.toArtist() }
    }
}
