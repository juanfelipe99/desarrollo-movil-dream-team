package com.equipo2.vinilos.ui.role

import android.content.Intent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.equipo2.vinilos.R
import com.equipo2.vinilos.data.model.Role
import com.equipo2.vinilos.databinding.ViewRoleChipBinding
import com.equipo2.vinilos.ui.theme.ThemeViewModel
import kotlinx.coroutines.launch

/** Chip de rol de la barra superior: muestra el rol actual y abre el menú de rol. */
class RoleChip(
    private val activity: AppCompatActivity,
    private val binding: ViewRoleChipBinding,
    private val onRoleChanged: (Role) -> Unit,
) {

    // Los mismos ViewModels de la actividad, que sobreviven cuando la pantalla se recrea
    private val roles: RoleViewModel by activity.viewModels { RoleViewModel.Factory }
    private val theme: ThemeViewModel by activity.viewModels { ThemeViewModel.Factory }

    private val menu = RoleMenu(
        anchor = binding.root,
        onRoleSelected = ::changeRole,
        onDarkModeChanged = theme::setDarkMode,
        onGoHome = ::goHome,
    )

    init {
        binding.root.setOnClickListener { openMenu() }

        activity.lifecycleScope.launch {
            activity.repeatOnLifecycle(Lifecycle.State.STARTED) { roles.role.collect(::show) }
        }

        // Si la pantalla se destruye con el menú abierto, por ejemplo al girar, el menú se cierra con ella.
        activity.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) = menu.dismiss()
        })
    }

    /** Mientras no hay rol, el chip no se ve. */
    private fun show(role: Role?) {
        binding.root.isVisible = role != null
        if (role == null) return
        val name = activity.getString(role.nameRes)
        binding.roleChipIcon.setImageResource(role.iconRes)
        binding.roleChipText.text = name
        binding.root.contentDescription = activity.getString(R.string.cd_change_role, name)
    }

    private fun openMenu() {
        val role = roles.role.value ?: return
        menu.show(role)
    }

    private fun changeRole(role: Role) {
        roles.changeRole(role)
        onRoleChanged(role)
    }

    /** Olvida el rol y abre la selección de rol cerrando la app, para que Atrás no regrese aquí. */
    private fun goHome() {
        activity.lifecycleScope.launch {
            roles.leave()
            activity.startActivity(
                Intent(activity, RoleSelectActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            )
        }
    }
}
