package com.equipo2.vinilos

import androidx.annotation.VisibleForTesting
import com.equipo2.vinilos.data.network.ApiClient
import com.equipo2.vinilos.data.network.album.AlbumApi
import com.equipo2.vinilos.data.network.album.AlbumServiceAdapter
import com.equipo2.vinilos.data.repository.AlbumRepository
import com.equipo2.vinilos.data.repository.AlbumRepositoryImpl

/** Crea cada dependencia una sola vez y la comparte. */
object ServiceLocator {

    private val apiClient by lazy { ApiClient(BuildConfig.API_BASE_URL) }

    private val albumServiceAdapter by lazy { AlbumServiceAdapter(apiClient.create(AlbumApi::class.java)) }
    private val defaultAlbumRepository by lazy { AlbumRepositoryImpl(albumServiceAdapter) }

    @VisibleForTesting
    var albumRepositoryOverride: AlbumRepository? = null

    val albumRepository: AlbumRepository
        get() = albumRepositoryOverride ?: defaultAlbumRepository
}
