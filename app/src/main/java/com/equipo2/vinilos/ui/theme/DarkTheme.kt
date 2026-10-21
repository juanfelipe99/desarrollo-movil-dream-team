package com.equipo2.vinilos.ui.theme

import android.content.Context
import android.content.res.Configuration

/** Si la pantalla se ve oscura en este momento, ya sea porque se eligió o porque lo dice el sistema. */
val Context.isDarkTheme: Boolean
    get() {
        // La máscara deja solo la configuración del modo noche
        val nightSetting = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return nightSetting == Configuration.UI_MODE_NIGHT_YES
    }
