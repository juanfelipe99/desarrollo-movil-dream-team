package com.equipo2.vinilos.data.local

import android.content.Context
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore

internal val Context.appDataStore by preferencesDataStore(
    name = "vinilos_prefs",
    // Si el archivo se daña, se reemplaza por uno vacío en lugar de cerrar la app
    corruptionHandler = ReplaceFileCorruptionHandler { emptyPreferences() },
)
