package com.equipo2.vinilos.data.local

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

/** Archivo del teléfono donde se guardan todas las preferencias. Cada repositorio usa sus propias claves. */
internal val Context.appPreferences by preferencesDataStore(name = "vinilos_prefs")
