package com.equipo2.vinilos

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
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

    /** Vista de la pantalla actual que los avisos no deben tapar, como el botón «+» de Álbumes. */
    var snackbarAnchor: View? = null

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

    /** Muestra un aviso encima de anchor, del snackbarAnchor o de la barra inferior, el primero que se vea. */
    fun showSnackbar(message: CharSequence, isError: Boolean = false, anchor: View? = null) {
        val target = listOf(anchor, snackbarAnchor, binding.bottomNav).firstOrNull { it?.isShown == true }
        AppSnackbar.show(binding.root, message, isError, target)
    }

    private fun setUpNavigation() {
        val navController = binding.navHost.getFragment<NavHostFragment>().navController
        binding.toolbar.setupWithNavController(navController, AppBarConfiguration(binding.bottomNav.menu))
        binding.bottomNav.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            showFullScreen(destination.id == R.id.albumCreateFragment)
        }
    }

    /** Pantalla completa: X para cerrar, sin chip de rol ni barra inferior. */
    private fun showFullScreen(fullScreen: Boolean) {
        binding.roleChipSlot.isVisible = !fullScreen
        binding.bottomNav.isVisible = !fullScreen
        binding.bottomNavDivider.isVisible = !fullScreen

        // X en lugar de la flecha de volver, que pone la navegación
        if (fullScreen) {
            binding.toolbar.setNavigationIcon(R.drawable.ic_close)
            binding.toolbar.setNavigationContentDescription(R.string.cd_close_without_saving)
        }
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
