package com.equipo2.vinilos

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.equipo2.vinilos.data.model.Role
import com.equipo2.vinilos.databinding.ActivityMainBinding
import com.equipo2.vinilos.ui.common.AppSnackbar
import com.equipo2.vinilos.ui.common.isDarkTheme
import com.equipo2.vinilos.ui.role.RoleMenu
import com.equipo2.vinilos.ui.role.RoleSelectActivity
import com.equipo2.vinilos.ui.role.changedMessageRes
import com.equipo2.vinilos.ui.role.show
import com.equipo2.vinilos.ui.session.SessionViewModel
import kotlinx.coroutines.launch
import kotlin.math.max

/** Barra superior, contenedor de navegación y barra inferior. */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var roleMenu: RoleMenu
    private val session: SessionViewModel by viewModels { SessionViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyWindowInsets()

        val navController = binding.navHost.getFragment<NavHostFragment>().navController
        binding.toolbar.setupWithNavController(navController, AppBarConfiguration(binding.bottomNav.menu))
        binding.bottomNav.setupWithNavController(navController)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                session.role.collect { binding.roleChip.show(it) }
            }
        }
        setUpRoleMenu(reopen = savedInstanceState?.getBoolean(KEY_ROLE_MENU_OPEN) == true)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(KEY_ROLE_MENU_OPEN, roleMenu.isShowing)
    }

    override fun onDestroy() {
        roleMenu.dismiss()
        super.onDestroy()
    }

    /** Muestra un aviso sobre el elemento inferior visible */
    fun showSnackbar(message: CharSequence, isError: Boolean = false, anchor: View? = null) {
        val target = anchor?.takeIf { it.isShown } ?: binding.bottomNav.takeIf { it.isShown }
        AppSnackbar.show(binding.root, message, isError, target)
    }

    /** Al cambiar el tema la pantalla se recrea, y si el menú estaba abierto se vuelve a abrir sin animación. */
    private fun setUpRoleMenu(reopen: Boolean) {
        roleMenu = RoleMenu(
            anchor = binding.roleChip.root,
            onRoleSelected = ::changeRole,
            // Se espera a que termine la animación del interruptor antes de recrear la pantalla.
            onDarkModeChanged = { enabled -> binding.root.postDelayed({ session.setDarkMode(enabled) }, SWITCH_ANIMATION_MS) },
            onGoHome = ::goHome,
        )
        binding.roleChip.root.setOnClickListener { showRoleMenu() }
        if (reopen) binding.roleChip.root.post { showRoleMenu(animate = false) }
    }

    private fun showRoleMenu(animate: Boolean = true) {
        val role = session.role.value ?: return
        roleMenu.show(role, isDarkTheme, animate)
    }

    private fun changeRole(role: Role) {
        if (role == session.role.value) return
        session.changeRole(role)
        showSnackbar(getString(role.changedMessageRes))
    }

    /** Vuelve a la selección de rol y cierra la app, para que Atrás no regrese aquí. */
    private fun goHome() {
        startActivity(
            Intent(this, RoleSelectActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        )
    }

    private fun applyWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.content) { view, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val keyboard = insets.getInsets(WindowInsetsCompat.Type.ime())
            view.setPadding(bars.left, bars.top, bars.right, max(bars.bottom, keyboard.bottom))
            WindowInsetsCompat.CONSUMED
        }
    }

    private companion object {
        const val KEY_ROLE_MENU_OPEN = "role_menu_open"
        const val SWITCH_ANIMATION_MS = 250L
    }
}
