package com.equipo2.vinilos.ui.role

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.equipo2.vinilos.R
import com.equipo2.vinilos.data.model.Role
import com.equipo2.vinilos.databinding.FragmentRoleSelectBinding
import com.equipo2.vinilos.ui.common.isDarkTheme
import com.equipo2.vinilos.ui.session.SessionViewModel

/** Primera pantalla de la app: se elige con qué rol entrar. */
class RoleSelectFragment : Fragment(R.layout.fragment_role_select) {

    private val session: SessionViewModel by activityViewModels { SessionViewModel.Factory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentRoleSelectBinding.bind(view)
        binding.actions.visitorButton.setOnClickListener { enterAs(Role.VISITOR) }
        binding.actions.collectorButton.setOnClickListener { enterAs(Role.COLLECTOR) }
        bindThemeButton(binding.themeButton)
    }

    private fun enterAs(role: Role) {
        val navController = findNavController()
        // Con dos toques rápidos el segundo llega cuando ya se salió de esta pantalla.
        if (navController.currentDestination?.id != R.id.roleSelectFragment) return
        session.enterAs(role)
        navController.navigate(R.id.action_roleSelect_to_albums)
    }

    /** Muestra la luna en el tema claro y el sol en el oscuro. */
    private fun bindThemeButton(button: ImageButton) {
        val isDark = requireContext().isDarkTheme
        button.setImageResource(if (isDark) R.drawable.ic_light_mode else R.drawable.ic_dark_mode)
        button.contentDescription = getString(if (isDark) R.string.cd_dark_mode_off else R.string.cd_dark_mode_on)
        button.setOnClickListener { session.setDarkMode(!isDark) }
    }
}
