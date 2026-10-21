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
import com.equipo2.vinilos.ui.common.isDarkTheme
import com.equipo2.vinilos.ui.session.SessionViewModel
import kotlinx.coroutines.launch

/** Primera pantalla de la app: se elige con qué rol entrar. */
class RoleSelectActivity : AppCompatActivity() {

    private val session: SessionViewModel by viewModels { SessionViewModel.Factory }
    private var entering = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityRoleSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.actions.visitorButton.setOnClickListener { enterAs(Role.VISITOR) }
        binding.actions.collectorButton.setOnClickListener { enterAs(Role.COLLECTOR) }
        bindThemeButton(binding.themeButton)
    }

    /** Abre la app cuando el rol ya está guardado. */
    private fun enterAs(role: Role) {
        if (entering) return
        entering = true
        lifecycleScope.launch {
            session.enterAs(role)
            startActivity(Intent(this@RoleSelectActivity, MainActivity::class.java))
            finish()
        }
    }

    /** Muestra la luna en el tema claro y el sol en el oscuro. */
    private fun bindThemeButton(button: ImageButton) {
        val isDark = isDarkTheme
        button.setImageResource(if (isDark) R.drawable.ic_light_mode else R.drawable.ic_dark_mode)
        button.contentDescription = getString(if (isDark) R.string.cd_dark_mode_off else R.string.cd_dark_mode_on)
        button.setOnClickListener { session.setDarkMode(!isDark) }
    }
}
