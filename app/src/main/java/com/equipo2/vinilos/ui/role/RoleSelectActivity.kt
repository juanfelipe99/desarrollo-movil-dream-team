package com.equipo2.vinilos.ui.role

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.equipo2.vinilos.MainActivity
import com.equipo2.vinilos.R
import com.equipo2.vinilos.data.model.Role
import com.equipo2.vinilos.databinding.ActivityRoleSelectBinding
import com.equipo2.vinilos.ui.theme.ThemeViewModel
import com.equipo2.vinilos.ui.theme.isDarkTheme
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/** Primera pantalla de la app: se elige con qué rol entrar. Si ya hay un rol guardado, abre la app directamente. */
class RoleSelectActivity : AppCompatActivity() {

    private val roles: RoleViewModel by viewModels { RoleViewModel.Factory }
    private val theme: ThemeViewModel by viewModels { ThemeViewModel.Factory }
    private var entering = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Se lee antes de dibujar para no mostrar esta pantalla un instante. Las preferencias ya están en memoria.
        if (runBlocking { roles.savedRole() } != null) {
            openApp()
            return
        }

        enableEdgeToEdge()
        val binding = ActivityRoleSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.actions.visitorButton.setOnClickListener { enterAs(Role.VISITOR) }
        binding.actions.collectorButton.setOnClickListener { enterAs(Role.COLLECTOR) }
        bindThemeButton(binding.themeButton)
    }

    /** Guarda el rol antes de abrir la app. Con un doble toque no entra dos veces. */
    private fun enterAs(role: Role) {
        if (entering) return
        entering = true
        lifecycleScope.launch {
            roles.enterAs(role)
            openApp()
        }
    }

    private fun openApp() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    /** Muestra la luna en el tema claro y el sol en el oscuro. */
    private fun bindThemeButton(button: ImageButton) {
        val isDark = isDarkTheme
        button.setImageResource(if (isDark) R.drawable.ic_light_mode else R.drawable.ic_dark_mode)
        button.contentDescription = getString(if (isDark) R.string.cd_dark_mode_off else R.string.cd_dark_mode_on)
        button.setOnClickListener { theme.setDarkMode(!isDark) }
    }
}
