package com.equipo2.vinilos.ui.common

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.widget.ImageViewCompat
import com.equipo2.vinilos.R
import com.google.android.material.snackbar.Snackbar

object AppSnackbar {

    private const val DURATION_MS = 4000

    fun show(
        root: View,
        message: CharSequence,
        isError: Boolean = false,
        anchor: View? = null,
        actionText: CharSequence? = null,
        action: (() -> Unit)? = null,
    ) {
        val snackbar = Snackbar.make(root, message, DURATION_MS)
        snackbar.anchorView = anchor
        if (actionText != null && action != null) snackbar.setAction(actionText) { action() }
        snackbar.customize(isError)
        snackbar.show()
    }

    /** Ajusta el texto del aviso y le agrega el ícono de estado y la X. */
    private fun Snackbar.customize(isError: Boolean) {
        val text = view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        text.maxLines = 4
        text.addStatusIcon(isError)
        text.addCloseButton { dismiss() }
    }

    /** El ícono va a la izquierda del texto y toma su mismo color. */
    private fun TextView.addStatusIcon(isError: Boolean) {
        val size = resources.getDimensionPixelSize(R.dimen.snackbar_icon)
        val icon = AppCompatResources.getDrawable(context, if (isError) R.drawable.ic_error else R.drawable.ic_check)!!.mutate()
        icon.setTint(currentTextColor)
        icon.setBounds(0, 0, size, size)
        setCompoundDrawablesRelative(icon, null, null, null)
        compoundDrawablePadding = resources.getDimensionPixelSize(R.dimen.snackbar_icon_padding)
    }

    /** Se agrega el botón de cerrar al final del contenido del aviso con el color del texto. */
    private fun TextView.addCloseButton(onClick: () -> Unit) {
        val content = parent as ViewGroup
        val close = LayoutInflater.from(context).inflate(R.layout.view_snackbar_close, content, false) as ImageView
        ImageViewCompat.setImageTintList(close, ColorStateList.valueOf(currentTextColor))
        close.setOnClickListener { onClick() }
        content.addView(close)
    }
}
