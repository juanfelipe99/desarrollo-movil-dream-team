package com.equipo2.vinilos.ui.role

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.PopupWindow
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isInvisible
import com.equipo2.vinilos.R
import com.equipo2.vinilos.data.model.Role
import com.equipo2.vinilos.databinding.PopupRoleMenuBinding

/**
 * Menú del chip de rol: rol actual, cambio de rol, modo oscuro y volver al inicio.
 * Es un PopupWindow con un layout propio porque un menú normal de Android no admite el interruptor.
 */
class RoleMenu(
    private val anchor: View,
    private val onRoleSelected: (Role) -> Unit,
    private val onDarkModeChanged: (Boolean) -> Unit,
    private val onGoHome: () -> Unit,
) {

    private var popup: PopupWindow? = null

    val isShowing: Boolean
        get() = popup?.isShowing == true

    /** Se abre sin animación cuando vuelve a aparecer después de cambiar el tema. */
    fun show(role: Role, isDark: Boolean, animate: Boolean = true) {
        val context = anchor.context
        val resources = context.resources
        val binding = PopupRoleMenuBinding.inflate(LayoutInflater.from(context))

        binding.menuHeader.text = context.getString(R.string.role_menu_current, context.getString(role.nameRes))
        bindOption(binding.optionVisitor, binding.checkVisitor, Role.VISITOR, role)
        bindOption(binding.optionCollector, binding.checkCollector, Role.COLLECTOR, role)
        binding.darkModeSwitch.isChecked = isDark
        binding.darkModeSwitch.setOnCheckedChangeListener { switch, checked ->
            switch.isEnabled = false
            onDarkModeChanged(checked)
        }
        binding.optionGoHome.setOnClickListener {
            dismiss()
            onGoHome()
        }

        val width = resources.getDimensionPixelSize(R.dimen.role_menu_width)
        val offset = resources.getDimensionPixelOffset(R.dimen.role_menu_offset)
        popup = PopupWindow(binding.root, width, ViewGroup.LayoutParams.WRAP_CONTENT, true).apply {
            setBackgroundDrawable(AppCompatResources.getDrawable(context, R.drawable.bg_role_menu))
            elevation = resources.getDimension(R.dimen.role_menu_elevation)
            if (!animate) animationStyle = 0
            // Si el menú no cabe debajo del chip, toma el alto disponible y se desplaza.
            binding.root.measure(
                View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            )
            height = minOf(binding.root.measuredHeight, getMaxAvailableHeight(anchor, offset))
            showAsDropDown(anchor, 0, offset, Gravity.END)
            dimBehind()
        }
    }

    fun dismiss() {
        popup?.dismiss()
        popup = null
    }

    /** La marca solo se ve en el rol actual, y TalkBack lo anuncia como seleccionado. */
    private fun bindOption(option: View, check: View, optionRole: Role, currentRole: Role) {
        val selected = optionRole == currentRole
        check.isInvisible = !selected
        option.isSelected = selected
        option.setOnClickListener {
            dismiss()
            onRoleSelected(optionRole)
        }
    }

    /** Oscurece la pantalla detrás del menú con el velo de Material para elementos modales. */
    private fun PopupWindow.dimBehind() {
        val window = contentView.rootView
        val params = window.layoutParams as WindowManager.LayoutParams
        params.flags = params.flags or WindowManager.LayoutParams.FLAG_DIM_BEHIND
        params.dimAmount = DIM_AMOUNT
        (anchor.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).updateViewLayout(window, params)
    }

    private companion object {
        const val DIM_AMOUNT = 0.32f
    }
}
