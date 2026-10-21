package com.equipo2.vinilos.data.repository

import com.equipo2.vinilos.data.local.ThemePreferences
import com.equipo2.vinilos.data.model.ThemeMode
import kotlinx.coroutines.flow.Flow

interface ThemeRepository {
    val themeMode: Flow<ThemeMode>
    suspend fun setThemeMode(mode: ThemeMode)
}

class ThemeRepositoryImpl(private val local: ThemePreferences) : ThemeRepository {

    override val themeMode = local.themeMode

    override suspend fun setThemeMode(mode: ThemeMode) = local.save(mode)
}
