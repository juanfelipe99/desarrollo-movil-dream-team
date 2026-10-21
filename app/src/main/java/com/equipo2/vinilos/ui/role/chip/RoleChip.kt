package com.equipo2.vinilos.ui.role.chip

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.equipo2.vinilos.data.model.Role
import com.equipo2.vinilos.databinding.ViewRoleChipBinding
import com.equipo2.vinilos.ui.role.RoleViewModel
import com.equipo2.vinilos.ui.role.menu.RoleMenu
import com.equipo2.vinilos.ui.role.select.RoleSelectActivity
import com.equipo2.vinilos.ui.theme.ThemeViewModel
import kotlinx.coroutines.launch

/** Chip de rol de la barra superior: muestra el rol actual y abre el menú de rol. */
class RoleChip(
    private val activity: AppCompatActivity,
    private val binding: ViewRoleChipBinding,
    private val onRoleChanged: (Role) -> Unit,
) {

    private val roleViewModel: RoleViewModel by activity.viewModels { RoleViewModel.Factory }
    private val themeViewModel: ThemeViewModel by activity.viewModels { ThemeViewModel.Factory }

    private val menu = RoleMenu(
        anchor = binding.root,
        lifecycle = activity.lifecycle,
        onRoleSelected = ::changeRole,
        onDarkModeChanged = themeViewModel::setDarkMode,
        onOpenRoleSelect = ::openRoleSelect,
    )

    init {
        // Abrir el menú
        binding.root.setOnClickListener { openMenu() }

        // El chip se actualiza cada vez que cambia el rol
        activity.lifecycleScope.launch {
            activity.repeatOnLifecycle(Lifecycle.State.STARTED) { roleViewModel.role.collect(binding::show) }
        }
    }

    private fun openMenu() {
        // Mientras se lee el rol, el chip no se ve ni abre el menú
        val role = roleViewModel.role.value ?: return
        menu.show(role)
    }

    private fun changeRole(role: Role) {
        roleViewModel.changeRole(role)
        onRoleChanged(role)
    }

    private fun openRoleSelect() {
        RoleSelectActivity.startFresh(activity)
    }
}
