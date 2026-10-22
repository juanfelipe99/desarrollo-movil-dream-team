package com.equipo2.vinilos.ui.thememode

import androidx.appcompat.app.AppCompatDelegate
import com.equipo2.vinilos.data.model.ThemeMode

/** Aplica el tema, y Android recrea las pantallas abiertas si la apariencia cambia. */
fun applyThemeMode(mode: ThemeMode) {
    AppCompatDelegate.setDefaultNightMode(mode.nightMode)
}

private val ThemeMode.nightMode: Int
    get() = when (this) {
        ThemeMode.SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        ThemeMode.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
        ThemeMode.DARK -> AppCompatDelegate.MODE_NIGHT_YES
    }
