package com.equipo2.vinilos

import android.content.Context
import com.equipo2.vinilos.data.local.RolePreferences
import com.equipo2.vinilos.data.local.ThemePreferences
import com.equipo2.vinilos.data.local.appPreferences
import com.equipo2.vinilos.data.network.ApiClient
import com.equipo2.vinilos.data.network.album.AlbumServiceAdapter
import com.equipo2.vinilos.data.repository.AlbumRepository
import com.equipo2.vinilos.data.repository.AlbumRepositoryImpl
import com.equipo2.vinilos.data.repository.RoleRepository
import com.equipo2.vinilos.data.repository.RoleRepositoryImpl
import com.equipo2.vinilos.data.repository.ThemeRepository
import com.equipo2.vinilos.data.repository.ThemeRepositoryImpl

/** Crea cada repositorio la primera vez que se pide y lo comparte. Las pruebas heredan de esta clase para cambiar alguno por uno falso. */
open class AppContainer(context: Context) {

    private val apiClient by lazy { ApiClient(BuildConfig.API_BASE_URL) }

    open val albumRepository: AlbumRepository by lazy { AlbumRepositoryImpl(AlbumServiceAdapter(apiClient)) }
    open val roleRepository: RoleRepository by lazy { RoleRepositoryImpl(RolePreferences(context.appPreferences)) }
    open val themeRepository: ThemeRepository by lazy { ThemeRepositoryImpl(ThemePreferences(context.appPreferences)) }
}
