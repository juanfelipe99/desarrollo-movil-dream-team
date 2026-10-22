package com.equipo2.vinilos.ui.role.select

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.equipo2.vinilos.R
import com.equipo2.vinilos.data.model.Role
import com.equipo2.vinilos.databinding.FragmentRoleSelectBinding
import com.equipo2.vinilos.ui.common.finishAppOnBack
import com.equipo2.vinilos.ui.role.RoleViewModel
import com.equipo2.vinilos.ui.thememode.ThemeModeViewModel
import com.equipo2.vinilos.ui.thememode.controls.bindThemeModeToggle

/** Primera pantalla: se elige con qué rol entrar y después se pasa a Álbumes. */
class RoleSelectFragment : Fragment(R.layout.fragment_role_select) {

    private val roleViewModel: RoleViewModel by activityViewModels { RoleViewModel.Factory }
    private val themeModeViewModel: ThemeModeViewModel by activityViewModels { ThemeModeViewModel.Factory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentRoleSelectBinding.bind(view)

        binding.actions.visitorButton.setOnClickListener { enterAs(Role.VISITOR) }
        binding.actions.collectorButton.setOnClickListener { enterAs(Role.COLLECTOR) }
        binding.themeModeButton.bindThemeModeToggle(themeModeViewModel, viewLifecycleOwner)

        // Atrás cierra la app en lugar de volver a Álbumes sin haber elegido rol
        finishAppOnBack()
    }

    /** Cambia el rol y quita esta pantalla de la pila, así queda Álbumes. */
    private fun enterAs(role: Role) {
        roleViewModel.changeRole(role)
        findNavController().popBackStack(R.id.roleSelectFragment, true)
    }
}
