package com.equipo2.vinilos.ui.state

import androidx.core.view.isVisible
import com.equipo2.vinilos.R
import com.equipo2.vinilos.databinding.ViewStateMessageBinding

/** Estado de error: «No pudimos cargar {objeto}» con el botón «Reintentar». */
fun ViewStateMessageBinding.showError(objectName: CharSequence, onRetry: () -> Unit) {
    val context = root.context

    stateIconCircle.isVisible = true
    stateIcon.setImageResource(R.drawable.ic_cloud_off)

    stateTitle.text = context.getString(R.string.error_load_title, objectName)
    stateBody.text = context.getString(R.string.error_network_body)

    stateRetry.isVisible = true
    stateRetry.setOnClickListener { onRetry() }
}
