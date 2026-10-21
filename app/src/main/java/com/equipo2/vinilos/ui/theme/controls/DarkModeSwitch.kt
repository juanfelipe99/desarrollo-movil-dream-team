package com.equipo2.vinilos.ui.theme.controls

import android.widget.CompoundButton
import com.equipo2.vinilos.ui.theme.isDarkTheme

private const val SWITCH_ANIMATION_MS = 250L

/** Interruptor de modo oscuro. */
fun CompoundButton.bindDarkModeSwitch(onChanged: (Boolean) -> Unit) {
    // Empieza según el tema actual
    isChecked = context.isDarkTheme

    // Espera la animación, porque el cambio de tema recrea la pantalla
    setOnCheckedChangeListener { switch, checked ->
        switch.isEnabled = false
        switch.postDelayed({ onChanged(checked) }, SWITCH_ANIMATION_MS)
    }
}
