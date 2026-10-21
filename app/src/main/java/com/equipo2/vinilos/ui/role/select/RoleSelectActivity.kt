package com.equipo2.vinilos.ui.role.select

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.equipo2.vinilos.MainActivity
import com.equipo2.vinilos.data.model.Role
import com.equipo2.vinilos.databinding.ActivityRoleSelectBinding
import com.equipo2.vinilos.ui.role.RoleViewModel
import com.equipo2.vinilos.ui.theme.ThemeViewModel
import com.equipo2.vinilos.ui.theme.controls.bindThemeToggle
import kotlinx.coroutines.launch

/** Primera pantalla: se elige con qué rol entrar. */
class RoleSelectActivity : AppCompatActivity() {

    private val roleViewModel: RoleViewModel by viewModels { RoleViewModel.Factory }
    private val themeViewModel: ThemeViewModel by viewModels { ThemeViewModel.Factory }
    private var isEntering = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Contenido
        enableEdgeToEdge()
        val binding = ActivityRoleSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Botones
        binding.actions.visitorButton.setOnClickListener { enterAs(Role.VISITOR) }
        binding.actions.collectorButton.setOnClickListener { enterAs(Role.COLLECTOR) }
        binding.themeButton.bindThemeToggle(themeViewModel)
    }

    /** Guarda el rol y abre la pantalla principal. Ignora el doble toque. */
    private fun enterAs(role: Role) {
        if (isEntering) return
        isEntering = true
        lifecycleScope.launch {
            roleViewModel.saveRole(role)
            openMainScreen()
        }
    }

    private fun openMainScreen() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    companion object {
        /** Abre esta pantalla cerrando las demás. */
        fun startFresh(context: Context) {
            context.startActivity(
                Intent(context, RoleSelectActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            )
        }
    }
}
