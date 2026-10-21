package com.equipo2.vinilos.ui.role.menu

import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import androidx.core.view.isInvisible
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.equipo2.vinilos.R
import com.equipo2.vinilos.data.model.Role
import com.equipo2.vinilos.databinding.PopupRoleMenuBinding
import com.equipo2.vinilos.ui.role.nameRes
import com.equipo2.vinilos.ui.theme.controls.bindDarkModeSwitch

/** Menú del chip de rol: rol actual, cambio de rol, modo oscuro y volver al inicio. */
class RoleMenu(
    private val anchor: View,
    lifecycle: Lifecycle,
    private val onRoleSelected: (Role) -> Unit,
    private val onDarkModeChanged: (Boolean) -> Unit,
    private val onOpenRoleSelect: () -> Unit,
) {

    private val context = anchor.context
    private var popup: PopupWindow? = null

    init {
        lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) = dismiss()
        })
    }

    fun show(role: Role) {
        val binding = PopupRoleMenuBinding.inflate(LayoutInflater.from(context))
        bind(binding, role)
        popup = showDropdown(anchor, binding.root)
    }

    fun dismiss() {
        popup?.dismiss()
        popup = null
    }

    private fun bind(binding: PopupRoleMenuBinding, role: Role) {
        // Encabezado: «MODO ACTUAL: …»
        binding.menuHeader.text = context.getString(R.string.role_menu_current, context.getString(role.nameRes))

        // Filas de Visitante y Coleccionista, con la marca en el actual
        bindOption(binding.optionVisitor, binding.checkVisitor, Role.VISITOR, role)
        bindOption(binding.optionCollector, binding.checkCollector, Role.COLLECTOR, role)

        // Interruptor «Modo oscuro»
        binding.darkModeSwitch.bindDarkModeSwitch { enabled ->
            dismiss()
            onDarkModeChanged(enabled)
        }

        // Fila «Volver a la pantalla de inicio»
        binding.optionRoleSelect.setOnClickListener {
            dismiss()
            onOpenRoleSelect()
        }
    }

    /** La marca solo se ve en el rol actual. Tocarlo solo cierra el menú. */
    private fun bindOption(option: View, check: View, optionRole: Role, currentRole: Role) {
        val selected = optionRole == currentRole

        // Marca ✓ y anuncio de TalkBack de «seleccionado»
        check.isInvisible = !selected
        option.isSelected = selected

        option.setOnClickListener {
            dismiss()
            if (!selected) onRoleSelected(optionRole)
        }
    }
}
