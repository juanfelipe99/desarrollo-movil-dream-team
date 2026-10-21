package com.equipo2.vinilos.ui.theme.controls

import android.widget.ImageButton
import com.equipo2.vinilos.R
import com.equipo2.vinilos.ui.theme.ThemeViewModel
import com.equipo2.vinilos.ui.theme.isDarkTheme

/** Botón de sol o luna que cambia al tema contrario. */
fun ImageButton.bindThemeToggle(themeViewModel: ThemeViewModel) {
    val isDark = context.isDarkTheme

    // Ícono: sol en el tema oscuro, luna en el claro
    setImageResource(if (isDark) R.drawable.ic_light_mode else R.drawable.ic_dark_mode)

    // Lo que lee TalkBack: «Activar modo claro» o «Activar modo oscuro»
    contentDescription = context.getString(if (isDark) R.string.cd_dark_mode_off else R.string.cd_dark_mode_on)

    // Al tocarlo, cambia al tema contrario
    setOnClickListener { themeViewModel.setDarkMode(!isDark) }
}
