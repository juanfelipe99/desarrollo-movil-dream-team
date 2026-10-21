package com.equipo2.vinilos.ui.role.menu

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.PopupWindow
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.getSystemService
import com.equipo2.vinilos.R

private const val DIM_AMOUNT = 0.32f

/** Muestra el contenido debajo del ancla, alineado a su derecha. */
internal fun showDropdown(anchor: View, content: View): PopupWindow {
    // Ancho y separación del ancla
    val resources = anchor.resources
    val width = resources.getDimensionPixelSize(R.dimen.role_menu_width)
    val offset = resources.getDimensionPixelOffset(R.dimen.role_menu_offset)

    // Se mide para saber si cabe debajo del ancla
    content.measure(
        View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
    )

    return PopupWindow(content, width, ViewGroup.LayoutParams.WRAP_CONTENT, true).apply {
        setBackgroundDrawable(AppCompatResources.getDrawable(anchor.context, R.drawable.bg_role_menu))
        elevation = resources.getDimension(R.dimen.role_menu_elevation)
        // Si no cabe, toma el alto disponible y se desplaza
        height = minOf(content.measuredHeight, getMaxAvailableHeight(anchor, offset))
        showAsDropDown(anchor, 0, offset, Gravity.END)
        dimBehind()
    }
}

/** Oscurece la pantalla detrás del menú. */
private fun PopupWindow.dimBehind() {
    val window = contentView.rootView
    val params = window.layoutParams as WindowManager.LayoutParams
    params.flags = params.flags or WindowManager.LayoutParams.FLAG_DIM_BEHIND
    params.dimAmount = DIM_AMOUNT
    contentView.context.getSystemService<WindowManager>()?.updateViewLayout(window, params)
}
