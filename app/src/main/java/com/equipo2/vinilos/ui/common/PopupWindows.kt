package com.equipo2.vinilos.ui.common

import android.view.WindowManager
import android.widget.PopupWindow
import androidx.core.content.getSystemService

/** Oscurece el fondo detrás de la ventana, y se llama después de mostrarla. */
fun PopupWindow.dimBehind(amount: Float) {
    // PopupWindow no oscurece el fondo, así que se cambian los parámetros de su ventana
    val window = contentView.rootView
    val params = window.layoutParams as WindowManager.LayoutParams
    params.flags = params.flags or WindowManager.LayoutParams.FLAG_DIM_BEHIND
    params.dimAmount = amount
    contentView.context.getSystemService<WindowManager>()?.updateViewLayout(window, params)
}
