package com.equipo2.vinilos

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.equipo2.vinilos.data.network.ApiClient
import com.equipo2.vinilos.data.network.album.AlbumServiceAdapter
import com.equipo2.vinilos.data.repository.AlbumRepository
import com.equipo2.vinilos.data.repository.AlbumRepositoryImpl
import com.equipo2.vinilos.data.repository.RoleRepository
import com.equipo2.vinilos.data.repository.RoleRepositoryImpl
import com.equipo2.vinilos.data.repository.ThemeRepository
import com.equipo2.vinilos.data.repository.ThemeRepositoryImpl

/** Crea cada repositorio una sola vez y lo comparte. */
object ServiceLocator {

    private lateinit var appContext: Context

    /** Se llama una vez desde VinilosApplication, antes de usar cualquier dependencia. */
    fun init(context: Context) {
        appContext = context.applicationContext
    }

    private val apiClient by lazy { ApiClient(BuildConfig.API_BASE_URL) }

    @set:VisibleForTesting
    var albumRepository: AlbumRepository by dependency { AlbumRepositoryImpl(AlbumServiceAdapter(apiClient)) }

    @set:VisibleForTesting
    var roleRepository: RoleRepository by dependency { RoleRepositoryImpl(appContext) }

    @set:VisibleForTesting
    var themeRepository: ThemeRepository by dependency { ThemeRepositoryImpl(appContext) }
}
