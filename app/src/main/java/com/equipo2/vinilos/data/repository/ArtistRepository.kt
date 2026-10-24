package com.equipo2.vinilos.data.repository

import com.equipo2.vinilos.data.model.Artist
import com.equipo2.vinilos.data.network.artist.ArtistServiceAdapter

interface ArtistRepository {
    suspend fun getArtists(): Result<List<Artist>>
}

class ArtistRepositoryImpl(private val serviceAdapter: ArtistServiceAdapter) : ArtistRepository {

    override suspend fun getArtists(): Result<List<Artist>> = resultOf { serviceAdapter.getArtists() }
}
