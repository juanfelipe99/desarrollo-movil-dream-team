package com.equipo2.vinilos.ui.common

import androidx.appcompat.app.AppCompatDelegate
import com.equipo2.vinilos.data.model.ThemeMode

/** Recrea las pantallas abiertas para que tomen los colores de values-night. */
fun applyThemeMode(mode: ThemeMode) {
    AppCompatDelegate.setDefaultNightMode(
        when (mode) {
            ThemeMode.SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            ThemeMode.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            ThemeMode.DARK -> AppCompatDelegate.MODE_NIGHT_YES
        }
    )
}
