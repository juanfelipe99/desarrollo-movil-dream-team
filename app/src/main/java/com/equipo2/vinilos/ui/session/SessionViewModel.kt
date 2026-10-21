package com.equipo2.vinilos.ui.session

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.equipo2.vinilos.VinilosApplication
import com.equipo2.vinilos.data.model.Role
import com.equipo2.vinilos.data.model.ThemeMode
import com.equipo2.vinilos.data.repository.RoleRepository
import com.equipo2.vinilos.data.repository.ThemeRepository
import com.equipo2.vinilos.ui.common.applyThemeMode
import kotlinx.coroutines.launch

/** Rol y tema de la app. Lo comparten la actividad y las pantallas que los cambian. */
class SessionViewModel(
    private val roleRepository: RoleRepository,
    private val themeRepository: ThemeRepository,
) : ViewModel() {

    suspend fun enterAs(role: Role) {
        roleRepository.setRole(role)
    }

    /** Guarda el tema y lo aplica. */
    fun setDarkMode(enabled: Boolean) {
        val mode = if (enabled) ThemeMode.DARK else ThemeMode.LIGHT
        viewModelScope.launch { themeRepository.setThemeMode(mode) }
        applyThemeMode(mode)
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val container = VinilosApplication.container
                SessionViewModel(container.roleRepository, container.themeRepository)
            }
        }
    }
}
