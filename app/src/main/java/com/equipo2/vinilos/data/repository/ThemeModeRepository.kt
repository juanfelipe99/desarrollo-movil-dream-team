package com.equipo2.vinilos.data.repository

import com.equipo2.vinilos.data.local.ThemeModeLocalDataSource
import com.equipo2.vinilos.data.model.ThemeMode
import kotlinx.coroutines.flow.Flow

interface ThemeModeRepository {
    val themeMode: Flow<ThemeMode>
    suspend fun setThemeMode(mode: ThemeMode)
}

class ThemeModeRepositoryImpl(private val localDataSource: ThemeModeLocalDataSource) : ThemeModeRepository {

    override val themeMode = localDataSource.themeMode

    override suspend fun setThemeMode(mode: ThemeMode) = localDataSource.save(mode)
}
