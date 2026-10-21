package com.equipo2.vinilos.ui.state

import androidx.annotation.DrawableRes
import androidx.core.view.isVisible
import com.equipo2.vinilos.databinding.ViewStateMessageBinding

/** EmptyState: título y texto, con el círculo del ícono solo si se pasa uno. */
fun ViewStateMessageBinding.showEmpty(title: CharSequence, body: CharSequence, @DrawableRes icon: Int? = null) {
    // Ícono
    stateIconCircle.isVisible = icon != null
    icon?.let(stateIcon::setImageResource)

    // Textos
    stateTitle.text = title
    stateBody.text = body

    // Sin botón, por si antes se mostró un error
    stateRetry.isVisible = false
}
