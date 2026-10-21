package com.equipo2.vinilos.ui.role

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.equipo2.vinilos.VinilosApplication
import com.equipo2.vinilos.data.model.Role
import com.equipo2.vinilos.data.repository.RoleRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RoleViewModel(private val roleRepository: RoleRepository) : ViewModel() {

    /** Rol actual. Es null solo mientras se lee. */
    val role: StateFlow<Role?> = roleRepository.role.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    /** Espera a que se guarde. */
    suspend fun saveRole(role: Role) {
        roleRepository.setRole(role)
    }

    /** Desde el menú, sin esperar a que se guarde. */
    fun changeRole(role: Role) {
        viewModelScope.launch { roleRepository.setRole(role) }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer { RoleViewModel(VinilosApplication.container.roleRepository) }
        }
    }
}
