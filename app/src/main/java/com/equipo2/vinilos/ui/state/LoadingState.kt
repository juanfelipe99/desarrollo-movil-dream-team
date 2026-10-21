package com.equipo2.vinilos.ui.state

import android.view.LayoutInflater
import androidx.annotation.LayoutRes
import com.equipo2.vinilos.databinding.ViewLoadingBinding

/** LoadingState: barra lineal, texto y el esqueleto de la pantalla. */
fun ViewLoadingBinding.showLoading(text: CharSequence, @LayoutRes skeletonLayout: Int) {
    // Texto
    loadingText.text = text

    // Esqueleto: se quita el anterior para no apilarlos al reintentar
    skeleton.removeAllViews()
    LayoutInflater.from(root.context).inflate(skeletonLayout, skeleton)
}
