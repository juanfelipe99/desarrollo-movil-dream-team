package com.equipo2.vinilos

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navOptions
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.equipo2.vinilos.databinding.ActivityMainBinding
import com.equipo2.vinilos.ui.common.AppSnackbar
import kotlin.math.max

/** Barra superior, contenedor de navegación y barra inferior. */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyWindowInsets()

        val navController = binding.navHost.getFragment<NavHostFragment>().navController
        binding.toolbar.setupWithNavController(navController, AppBarConfiguration(binding.bottomNav.menu))
        setUpBottomNav(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            showBars(destination.id != R.id.roleSelectFragment)
        }
    }

    /** Muestra un aviso sobre el elemento inferior visible */
    fun showSnackbar(message: CharSequence, isError: Boolean = false, anchor: View? = null) {
        val target = anchor?.takeIf { it.isShown } ?: binding.bottomNav.takeIf { it.isShown }
        AppSnackbar.show(binding.root, message, isError, target)
    }

    /** La pantalla inicial es la selección de rol y se borra al entrar, así que cada pestaña vuelve a Álbumes y no a ella. */
    private fun setUpBottomNav(navController: NavController) {
        binding.bottomNav.setupWithNavController(navController)
        binding.bottomNav.setOnItemSelectedListener { item ->
            navController.navigate(item.itemId, null, navOptions {
                launchSingleTop = true
                restoreState = true
                popUpTo(R.id.albumListFragment) { saveState = true }
            })
            true
        }
    }

    /** La selección de rol ocupa toda la pantalla, sin barras. */
    private fun showBars(visible: Boolean) {
        binding.toolbar.isVisible = visible
        binding.toolbarDivider.isVisible = visible
        binding.bottomNav.isVisible = visible
        binding.bottomNavDivider.isVisible = visible
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
