package com.equipo2.vinilos

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.equipo2.vinilos.databinding.ActivityMainBinding
import com.equipo2.vinilos.ui.common.AppSnackbar
import com.equipo2.vinilos.ui.role.chip.RoleChip
import com.equipo2.vinilos.ui.role.changedMessageRes
import kotlin.math.max

/** Barra superior, contenedor de navegación y barra inferior. */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Contenido de punta a punta, con márgenes para las barras del sistema y el teclado
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyWindowInsets()

        // Pestañas y chip de rol, que avisa cuando cambia el rol
        setUpNavigation()
        RoleChip(this, binding.roleChip, onRoleChanged = { role -> showSnackbar(getString(role.changedMessageRes)) })
    }

    /** Muestra un aviso encima de la barra inferior, o encima de anchor si se pasa. */
    fun showSnackbar(message: CharSequence, isError: Boolean = false, anchor: View? = null) {
        val target = anchor?.takeIf { it.isShown } ?: binding.bottomNav.takeIf { it.isShown }
        AppSnackbar.show(binding.root, message, isError, target)
    }

    private fun setUpNavigation() {
        val navController = binding.navHost.getFragment<NavHostFragment>().navController
        binding.toolbar.setupWithNavController(navController, AppBarConfiguration(binding.bottomNav.menu))
        binding.bottomNav.setupWithNavController(navController)
    }

    private fun applyWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.content) { view, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val keyboard = insets.getInsets(WindowInsetsCompat.Type.ime())
            view.setPadding(bars.left, bars.top, bars.right, max(bars.bottom, keyboard.bottom))
            WindowInsetsCompat.CONSUMED
        }
    }
}
