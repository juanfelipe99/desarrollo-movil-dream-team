package com.equipo2.vinilos.ui.common

import android.view.LayoutInflater
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.core.view.isVisible
import com.equipo2.vinilos.R
import com.equipo2.vinilos.databinding.ViewLoadingBinding
import com.equipo2.vinilos.databinding.ViewStateMessageBinding

/** LoadingState: barra lineal, texto y el esqueleto de la pantalla, como view_skeleton_grid, view_skeleton_rows o view_skeleton_detail. */
fun ViewLoadingBinding.showLoading(text: CharSequence, @LayoutRes skeletonLayout: Int) {
    loadingText.text = text
    skeleton.removeAllViews()
    LayoutInflater.from(root.context).inflate(skeletonLayout, skeleton)
}

/** EmptyState: título y texto, con el círculo del ícono solo si se pasa uno. */
fun ViewStateMessageBinding.showEmpty(title: CharSequence, body: CharSequence, @DrawableRes icon: Int? = null) {
    stateIconCircle.isVisible = icon != null
    icon?.let(stateIcon::setImageResource)
    stateTitle.text = title
    stateBody.text = body
    stateRetry.isVisible = false
}

/** ErrorState: «No pudimos cargar {objeto}» con el botón «Reintentar». */
fun ViewStateMessageBinding.showError(objectName: CharSequence, onRetry: () -> Unit) {
    val context = root.context
    stateIconCircle.isVisible = true
    stateIcon.setImageResource(R.drawable.ic_cloud_off)
    stateTitle.text = context.getString(R.string.error_load_title, objectName)
    stateBody.text = context.getString(R.string.error_network_body)
    stateRetry.isVisible = true
    stateRetry.setOnClickListener { onRetry() }
}
