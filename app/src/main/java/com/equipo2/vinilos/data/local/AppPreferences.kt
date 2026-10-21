package com.equipo2.vinilos.data.local

import android.content.Context
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore

/** Archivo del teléfono donde se guardan todas las preferencias. */
internal val Context.appPreferences by preferencesDataStore(
    name = "vinilos_prefs",
    corruptionHandler = ReplaceFileCorruptionHandler { emptyPreferences() },
)
