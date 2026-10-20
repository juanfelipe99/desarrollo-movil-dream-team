package com.equipo2.vinilos.ui.common

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.widget.ImageViewCompat
import com.equipo2.vinilos.R
import com.google.android.material.snackbar.Snackbar
import java.lang.ref.WeakReference

object AppSnackbar {

    private const val DURATION_MS = 4000

    private var current: WeakReference<Snackbar>? = null

    fun show(
        root: View,
        message: CharSequence,
        isError: Boolean = false,
        anchor: View? = null,
        actionText: CharSequence? = null,
        action: (() -> Unit)? = null,
    ) {
        dismissCurrent()
        val snackbar = Snackbar.make(root, message, DURATION_MS)
        snackbar.anchorView = anchor
        if (actionText != null && action != null) snackbar.setAction(actionText) { action() }
        snackbar.addStatusIcon(isError)
        snackbar.addCloseButton()
        current = WeakReference(snackbar)
        snackbar.show()
    }

    /** Un aviso nuevo reemplaza al anterior de inmediato, sin esperar su animación de salida. */
    private fun dismissCurrent() {
        current?.get()?.let { previous ->
            previous.view.visibility = View.INVISIBLE
            previous.dismiss()
        }
    }

    private val Snackbar.textView: TextView
        get() = view.findViewById(com.google.android.material.R.id.snackbar_text)

    /** El ícono va a la izquierda del texto y toma su mismo color. */
    private fun Snackbar.addStatusIcon(isError: Boolean) {
        val text = textView
        val size = context.resources.getDimensionPixelSize(R.dimen.snackbar_icon)
        val icon = AppCompatResources.getDrawable(context, if (isError) R.drawable.ic_error else R.drawable.ic_check)!!.mutate()
        DrawableCompat.setTint(icon, text.currentTextColor)
        icon.setBounds(0, 0, size, size)
        text.setCompoundDrawablesRelative(icon, null, null, null)
        text.compoundDrawablePadding = context.resources.getDimensionPixelSize(R.dimen.snackbar_icon_padding)
        text.maxLines = 4
    }

    /** Material no trae botón de cerrar, así que se agrega al final del contenido del aviso con el color del texto. */
    private fun Snackbar.addCloseButton() {
        val content = textView.parent as ViewGroup
        val close = LayoutInflater.from(context).inflate(R.layout.view_snackbar_close, content, false) as ImageView
        ImageViewCompat.setImageTintList(close, ColorStateList.valueOf(textView.currentTextColor))
        close.setOnClickListener { dismiss() }
        content.addView(close)
    }
}
