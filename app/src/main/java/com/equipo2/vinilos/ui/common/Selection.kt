package com.equipo2.vinilos.ui.common

import android.view.View
import androidx.core.view.isInvisible

/** Marca la opción como elegida: muestra su ✓ y TalkBack la anuncia como seleccionada. */
fun View.markSelected(check: View, selected: Boolean) {
    check.isInvisible = !selected
    isSelected = selected
}
