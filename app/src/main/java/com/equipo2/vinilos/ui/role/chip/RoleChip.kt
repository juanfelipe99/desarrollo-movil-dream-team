package com.equipo2.vinilos.ui.role.chip

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.equipo2.vinilos.R
import com.equipo2.vinilos.data.model.Role
import com.equipo2.vinilos.data.model.ThemeMode
import com.equipo2.vinilos.databinding.ViewRoleChipBinding
import com.equipo2.vinilos.ui.common.collectWhileVisible
import com.equipo2.vinilos.ui.common.navigateIfPossible
import com.equipo2.vinilos.ui.role.RoleViewModel
import com.equipo2.vinilos.ui.role.menu.RoleMenu
import com.equipo2.vinilos.ui.thememode.ThemeModeViewModel

/** Chip de rol de la barra superior: muestra el rol actual y abre el menú de rol. */
class RoleChip(
    private val activity: AppCompatActivity,
    private val binding: ViewRoleChipBinding,
    private val onRoleChanged: (Role) -> Unit,
) {

    private val roleViewModel: RoleViewModel by activity.viewModels { RoleViewModel.Factory }
    private val themeModeViewModel: ThemeModeViewModel by activity.viewModels { ThemeModeViewModel.Factory }

    private val menu = RoleMenu(
        anchor = binding.root,
        lifecycle = activity.lifecycle,
        onRoleSelected = ::changeRole,
        onThemeModeSelected = themeModeViewModel::setThemeMode,
        onOpenRoleSelect = ::openRoleSelect,
    )

    init {
        binding.root.setOnClickListener { openMenu() }

        activity.collectWhileVisible(roleViewModel.role, binding::show)
    }

    /** Abre el menú con el rol y el tema actuales marcados, salvo mientras se lee el rol. */
    private fun openMenu() {
        val role = roleViewModel.role.value ?: return
        menu.show(role, themeModeViewModel.themeMode.value ?: ThemeMode.SYSTEM)
    }

    private fun changeRole(role: Role) {
        roleViewModel.changeRole(role)
        onRoleChanged(role)
    }

    private fun openRoleSelect() {
        activity.findNavController(R.id.navHost).navigateIfPossible(R.id.action_global_roleSelect)
    }
}
