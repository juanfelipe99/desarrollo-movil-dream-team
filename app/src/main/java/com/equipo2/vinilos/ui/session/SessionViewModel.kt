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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/** Rol y tema de la app. Lo usan la selección de rol y la barra superior de la app. */
class SessionViewModel(
    private val roleRepository: RoleRepository,
    private val themeRepository: ThemeRepository,
) : ViewModel() {

    /** El rol guardado. Es null mientras se lee o si todavía no se eligió ninguno. */
    val role: StateFlow<Role?> = roleRepository.role.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

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
