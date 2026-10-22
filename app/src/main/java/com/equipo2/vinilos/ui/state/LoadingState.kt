package com.equipo2.vinilos.ui.state

import android.view.LayoutInflater
import androidx.annotation.LayoutRes
import com.equipo2.vinilos.databinding.ViewLoadingBinding

/** Estado de carga: barra, texto y el esqueleto de la pantalla. */
fun ViewLoadingBinding.showLoading(text: CharSequence, @LayoutRes skeletonLayout: Int) {
    loadingText.text = text

    // Se quita el esqueleto anterior para no apilarlos al reintentar
    skeleton.removeAllViews()
    LayoutInflater.from(root.context).inflate(skeletonLayout, skeleton)
}
