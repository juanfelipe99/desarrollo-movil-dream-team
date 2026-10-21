package com.equipo2.vinilos.ui.role

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.equipo2.vinilos.VinilosApplication
import com.equipo2.vinilos.data.model.Role
import com.equipo2.vinilos.data.repository.RoleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RoleViewModel(private val roleRepository: RoleRepository) : ViewModel() {

    private val currentRole = MutableStateFlow<Role?>(null)

    /** Rol actual. Es null solo mientras se lee. */
    val role: StateFlow<Role?> = currentRole

    init {
        viewModelScope.launch { roleRepository.role.collect { currentRole.value = it } }
    }

    /** Espera a que se guarde. */
    suspend fun saveRole(role: Role) {
        roleRepository.setRole(role)
    }

    /** Desde el menú. La pantalla cambia al instante, antes de que termine el guardado. */
    fun changeRole(role: Role) {
        currentRole.value = role
        viewModelScope.launch { roleRepository.setRole(role) }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer { RoleViewModel(VinilosApplication.container.roleRepository) }
        }
    }
}
