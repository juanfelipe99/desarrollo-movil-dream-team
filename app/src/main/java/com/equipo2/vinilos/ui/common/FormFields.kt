package com.equipo2.vinilos.ui.common

import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.content.res.AppCompatResources
import com.equipo2.vinilos.R
import com.google.android.material.textfield.TextInputLayout

/** Muestra debajo del campo el mensaje con el ícono de error y el borde de 2 dp, o lo quita con null. */
fun TextInputLayout.showError(@StringRes message: Int?) {
    if (message == null) {
        // Sin error tampoco queda reservado su espacio, y vuelve la ayuda si el campo tiene
        isErrorEnabled = false
        boxStrokeWidth = resources.getDimensionPixelSize(R.dimen.field_stroke)
        return
    }

    error = context.getString(message)
    boxStrokeWidth = resources.getDimensionPixelSize(R.dimen.field_stroke_error)

    // El texto del error es una vista interna de Material, por eso se busca por su id
    val text = findViewById<TextView>(com.google.android.material.R.id.textinput_error) ?: return
    val icon = AppCompatResources.getDrawable(context, R.drawable.ic_error)!!.mutate()
    icon.setTint(text.currentTextColor)

    val size = resources.getDimensionPixelSize(R.dimen.field_error_icon)
    icon.setBounds(0, 0, size, size)
    text.setCompoundDrawablesRelative(icon, null, null, null)
    text.compoundDrawablePadding = resources.getDimensionPixelSize(R.dimen.field_error_icon_padding)
}
