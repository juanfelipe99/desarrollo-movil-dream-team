package com.equipo2.vinilos.ui.role

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.PopupWindow
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.getSystemService
import androidx.core.view.isInvisible
import com.equipo2.vinilos.R
import com.equipo2.vinilos.data.model.Role
import com.equipo2.vinilos.databinding.PopupRoleMenuBinding
import com.equipo2.vinilos.ui.theme.isDarkTheme

/** Menú del chip de rol: rol actual, cambio de rol, modo oscuro y volver al inicio. */
class RoleMenu(
    private val anchor: View,
    private val onRoleSelected: (Role) -> Unit,
    private val onDarkModeChanged: (Boolean) -> Unit,
    private val onGoHome: () -> Unit,
) {

    private val context = anchor.context
    private var popup: PopupWindow? = null

    fun show(role: Role) {
        val binding = PopupRoleMenuBinding.inflate(LayoutInflater.from(context))
        bind(binding, role)
        popup = createPopup(binding.root)
    }

    fun dismiss() {
        popup?.dismiss()
        popup = null
    }

    private fun bind(binding: PopupRoleMenuBinding, role: Role) {
        binding.menuHeader.text = context.getString(R.string.role_menu_current, context.getString(role.nameRes))

        bindOption(binding.optionVisitor, binding.checkVisitor, Role.VISITOR, role)
        bindOption(binding.optionCollector, binding.checkCollector, Role.COLLECTOR, role)

        binding.darkModeSwitch.isChecked = context.isDarkTheme
        // Se deja ver la animación del interruptor antes de cerrar el menú y cambiar el tema.
        binding.darkModeSwitch.setOnCheckedChangeListener { switch, checked ->
            switch.isEnabled = false
            switch.postDelayed({
                dismiss()
                onDarkModeChanged(checked)
            }, SWITCH_ANIMATION_MS)
        }

        binding.optionGoHome.setOnClickListener {
            dismiss()
            onGoHome()
        }
    }

    /** La marca solo se ve en el rol actual. Tocarlo solo cierra el menú. */
    private fun bindOption(option: View, check: View, optionRole: Role, currentRole: Role) {
        val selected = optionRole == currentRole
        check.isInvisible = !selected
        option.isSelected = selected
        option.setOnClickListener {
            dismiss()
            if (!selected) onRoleSelected(optionRole)
        }
    }

    /** Si el menú no cabe debajo del chip, toma el alto disponible y se desplaza. */
    private fun createPopup(content: View): PopupWindow {
        val resources = context.resources
        val width = resources.getDimensionPixelSize(R.dimen.role_menu_width)
        val offset = resources.getDimensionPixelOffset(R.dimen.role_menu_offset)
        content.measure(
            View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
        )
        return PopupWindow(content, width, ViewGroup.LayoutParams.WRAP_CONTENT, true).apply {
            setBackgroundDrawable(AppCompatResources.getDrawable(context, R.drawable.bg_role_menu))
            elevation = resources.getDimension(R.dimen.role_menu_elevation)
            height = minOf(content.measuredHeight, getMaxAvailableHeight(anchor, offset))
            showAsDropDown(anchor, 0, offset, Gravity.END)
            dimBehind()
        }
    }

    /** Oscurece la pantalla detrás del menú con el velo de Material para elementos modales. */
    private fun PopupWindow.dimBehind() {
        val window = contentView.rootView
        val params = window.layoutParams as WindowManager.LayoutParams
        params.flags = params.flags or WindowManager.LayoutParams.FLAG_DIM_BEHIND
        params.dimAmount = DIM_AMOUNT
        context.getSystemService<WindowManager>()?.updateViewLayout(window, params)
    }

    private companion object {
        const val DIM_AMOUNT = 0.32f
        const val SWITCH_ANIMATION_MS = 250L
    }
}
