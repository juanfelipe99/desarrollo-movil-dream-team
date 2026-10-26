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

/** Rol de la app, compartido por la selección de rol, el chip y las pantallas. */
class RoleViewModel(private val repository: RoleRepository) : ViewModel() {

    private val currentRole = MutableStateFlow<Role?>(null)

    val role: StateFlow<Role?> = currentRole

    init {
        viewModelScope.launch { repository.role.collect { currentRole.value = it } }
    }

    /** Cambia el rol al instante y lo guarda en segundo plano. */
    fun changeRole(role: Role) {
        currentRole.value = role
        viewModelScope.launch { repository.setRole(role) }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer { RoleViewModel(VinilosApplication.container.roleRepository) }
        }
    }
}
