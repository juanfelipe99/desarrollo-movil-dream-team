package com.equipo2.vinilos

import androidx.annotation.VisibleForTesting
import com.equipo2.vinilos.data.network.ApiClient
import com.equipo2.vinilos.data.network.album.AlbumServiceAdapter
import com.equipo2.vinilos.data.repository.AlbumRepository
import com.equipo2.vinilos.data.repository.AlbumRepositoryImpl

/** Crea cada repositorio una sola vez y lo comparte. */
object ServiceLocator {

    private val apiClient by lazy { ApiClient(BuildConfig.API_BASE_URL) }

    @set:VisibleForTesting
    var albumRepository: AlbumRepository by dependency { AlbumRepositoryImpl(AlbumServiceAdapter(apiClient)) }
}
