package com.equipo2.vinilos.ui.thememode

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.equipo2.vinilos.R
import com.equipo2.vinilos.data.model.ThemeMode

/** Nombre del tema: «Claro», «Oscuro» o «Del sistema». */
@get:StringRes
val ThemeMode.nameRes: Int
    get() = when (this) {
        ThemeMode.LIGHT -> R.string.theme_mode_light
        ThemeMode.DARK -> R.string.theme_mode_dark
        ThemeMode.SYSTEM -> R.string.theme_mode_system
    }

/** Ícono del tema: sol, luna o contraste. */
@get:DrawableRes
val ThemeMode.iconRes: Int
    get() = when (this) {
        ThemeMode.LIGHT -> R.drawable.ic_light_mode
        ThemeMode.DARK -> R.drawable.ic_dark_mode
        ThemeMode.SYSTEM -> R.drawable.ic_contrast
    }
