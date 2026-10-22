package com.equipo2.vinilos.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.equipo2.vinilos.data.model.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ThemeModeLocalDataSource(private val dataStore: DataStore<Preferences>) {

    val themeMode: Flow<ThemeMode> = dataStore.data.map { prefs ->
        ThemeMode.entries.find { it.name == prefs[THEME_MODE_KEY] } ?: ThemeMode.SYSTEM
    }

    suspend fun save(mode: ThemeMode) {
        dataStore.edit { it[THEME_MODE_KEY] = mode.name }
    }
}

private val THEME_MODE_KEY = stringPreferencesKey("theme_mode")
