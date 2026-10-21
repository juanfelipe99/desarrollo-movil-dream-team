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
        binding.bottomNav.setupWithNavController(navController)
    }

    /** Muestra un aviso sobre el elemento inferior visible */
    fun showSnackbar(message: CharSequence, isError: Boolean = false, anchor: View? = null) {
        val target = anchor?.takeIf { it.isShown } ?: binding.bottomNav.takeIf { it.isShown }
        AppSnackbar.show(binding.root, message, isError, target)
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
