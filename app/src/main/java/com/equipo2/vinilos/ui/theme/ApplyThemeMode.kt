package com.equipo2.vinilos.ui.theme

import androidx.appcompat.app.AppCompatDelegate
import com.equipo2.vinilos.data.model.ThemeMode

/** Aplica el tema sin guardarlo. Recrea las pantallas abiertas. */
fun applyThemeMode(mode: ThemeMode) {
    AppCompatDelegate.setDefaultNightMode(mode.nightMode)
}

/** Valor equivalente en AppCompat. */
private val ThemeMode.nightMode: Int
    get() = when (this) {
        ThemeMode.SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        ThemeMode.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
        ThemeMode.DARK -> AppCompatDelegate.MODE_NIGHT_YES
    }
