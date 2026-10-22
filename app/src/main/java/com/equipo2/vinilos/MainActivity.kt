package com.equipo2.vinilos

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.equipo2.vinilos.databinding.ActivityMainBinding
import com.equipo2.vinilos.ui.common.AppSnackbar
import com.equipo2.vinilos.ui.common.applySystemBarsPadding
import com.equipo2.vinilos.ui.role.changedMessageRes
import com.equipo2.vinilos.ui.role.chip.RoleChip

/** Única actividad de la app: contiene la barra superior, las pantallas y la barra inferior. */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    var snackbarAnchor: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.applySystemBarsPadding()

        setUpNavigation(firstLaunch = savedInstanceState == null)
        RoleChip(this, binding.roleChip, onRoleChanged = { role -> showSnackbar(getString(role.changedMessageRes)) })
    }

    /** Muestra un aviso encima de la primera vista visible entre anchor, snackbarAnchor y la barra inferior. */
    fun showSnackbar(message: CharSequence, isError: Boolean = false, anchor: View? = null) {
        val target = listOf(anchor, snackbarAnchor, binding.bottomNav).firstOrNull { it?.isShown == true }
        AppSnackbar.show(binding.root, message, isError, target)
    }

    private fun setUpNavigation(firstLaunch: Boolean) {
        val navController = binding.navHost.getFragment<NavHostFragment>().navController
        binding.toolbar.setupWithNavController(navController, AppBarConfiguration(binding.bottomNav.menu))
        binding.bottomNav.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ -> updateBars(destination.id) }

        // La app abre en la selección de rol, y al recrearse la navegación recuerda dónde estaba
        if (firstLaunch) navController.navigate(R.id.action_global_roleSelect)
    }

    /** Oculta lo que no lleva cada pantalla */
    private fun updateBars(@IdRes destinationId: Int) {
        val roleSelect = destinationId == R.id.roleSelectFragment
        val albumCreate = destinationId == R.id.albumCreateFragment

        binding.toolbar.isVisible = !roleSelect
        binding.toolbarDivider.isVisible = !roleSelect
        binding.roleChipSlot.isVisible = !albumCreate

        binding.bottomNav.isVisible = !roleSelect && !albumCreate
        binding.bottomNavDivider.isVisible = binding.bottomNav.isVisible

        // La navegación pone una flecha de volver, y Crear álbum lleva una X en su lugar
        if (albumCreate) {
            binding.toolbar.setNavigationIcon(R.drawable.ic_close)
            binding.toolbar.setNavigationContentDescription(R.string.cd_close_without_saving)
        }
    }
}
