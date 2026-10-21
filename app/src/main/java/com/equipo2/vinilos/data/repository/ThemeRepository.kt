package com.equipo2.vinilos.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.equipo2.vinilos.data.model.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface ThemeRepository {
    val themeMode: Flow<ThemeMode>
    suspend fun setThemeMode(mode: ThemeMode)
}

class ThemeRepositoryImpl(private val preferences: DataStore<Preferences>) : ThemeRepository {

    override val themeMode = preferences.data.map { prefs ->
        ThemeMode.entries.find { it.name == prefs[THEME_MODE_KEY] } ?: ThemeMode.SYSTEM
    }

    override suspend fun setThemeMode(mode: ThemeMode) {
        preferences.edit { it[THEME_MODE_KEY] = mode.name }
    }
}

private val THEME_MODE_KEY = stringPreferencesKey("theme_mode")
