package com.equipo2.vinilos.ui.role.menu

import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import androidx.lifecycle.Lifecycle
import com.equipo2.vinilos.R
import com.equipo2.vinilos.data.model.Role
import com.equipo2.vinilos.data.model.ThemeMode
import com.equipo2.vinilos.databinding.PopupRoleMenuBinding
import com.equipo2.vinilos.ui.common.doOnDestroy
import com.equipo2.vinilos.ui.common.markSelected
import com.equipo2.vinilos.ui.role.nameRes

/** Menú del chip de rol: rol actual, cambio de rol, tema y volver a la pantalla de inicio. */
class RoleMenu(
    private val anchor: View,
    lifecycle: Lifecycle,
    private val onRoleSelected: (Role) -> Unit,
    private val onThemeModeSelected: (ThemeMode) -> Unit,
    private val onOpenRoleSelect: () -> Unit,
) {

    private val context = anchor.context
    private var popup: PopupWindow? = null

    init {
        // Se cierra junto con la pantalla, por ejemplo al girarla
        lifecycle.doOnDestroy(::dismiss)
    }

    fun show(role: Role, themeMode: ThemeMode) {
        val binding = PopupRoleMenuBinding.inflate(LayoutInflater.from(context))
        bind(binding, role, themeMode)
        popup = showDropdown(anchor, binding.root)
    }

    fun dismiss() {
        popup?.dismiss()
        popup = null
    }

    private fun bind(binding: PopupRoleMenuBinding, role: Role, themeMode: ThemeMode) {
        binding.menuHeader.text = context.getString(R.string.role_menu_current, context.getString(role.nameRes))

        bindOption(binding.optionVisitor, binding.checkVisitor, role == Role.VISITOR) { onRoleSelected(Role.VISITOR) }
        bindOption(binding.optionCollector, binding.checkCollector, role == Role.COLLECTOR) { onRoleSelected(Role.COLLECTOR) }

        bindOption(binding.optionLight, binding.checkLight, themeMode == ThemeMode.LIGHT) { onThemeModeSelected(ThemeMode.LIGHT) }
        bindOption(binding.optionDark, binding.checkDark, themeMode == ThemeMode.DARK) { onThemeModeSelected(ThemeMode.DARK) }
        bindOption(binding.optionSystem, binding.checkSystem, themeMode == ThemeMode.SYSTEM) { onThemeModeSelected(ThemeMode.SYSTEM) }

        binding.optionRoleSelect.setOnClickListener {
            dismiss()
            onOpenRoleSelect()
        }
    }

    /** La marca aparece en la opción elegida, y tocarla solo cierra el menú. */
    private fun bindOption(option: View, check: View, selected: Boolean, onSelected: () -> Unit) {
        option.markSelected(check, selected)

        option.setOnClickListener {
            dismiss()
            if (!selected) onSelected()
        }
    }
}
