package com.equipo2.vinilos.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.equipo2.vinilos.data.local.appPreferences
import com.equipo2.vinilos.data.model.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface ThemeRepository {
    val themeMode: Flow<ThemeMode>
    suspend fun setThemeMode(mode: ThemeMode)
}

class ThemeRepositoryImpl(context: Context) : ThemeRepository {

    private val preferences = context.appPreferences

    override val themeMode = preferences.data.map { prefs ->
        ThemeMode.entries.find { it.name == prefs[THEME_MODE_KEY] } ?: ThemeMode.SYSTEM
    }

    override suspend fun setThemeMode(mode: ThemeMode) {
        preferences.edit { it[THEME_MODE_KEY] = mode.name }
    }
}

private val THEME_MODE_KEY = stringPreferencesKey("theme_mode")
