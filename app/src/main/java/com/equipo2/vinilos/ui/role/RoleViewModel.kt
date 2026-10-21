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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RoleViewModel(private val roleRepository: RoleRepository) : ViewModel() {

    /** El rol guardado. Es null mientras se lee o si todavía no se eligió ninguno. */
    val role: StateFlow<Role?> = roleRepository.role.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    suspend fun savedRole(): Role? = roleRepository.role.first()

    suspend fun enterAs(role: Role) {
        roleRepository.setRole(role)
    }

    suspend fun leave() {
        roleRepository.clearRole()
    }

    fun changeRole(role: Role) {
        viewModelScope.launch { roleRepository.setRole(role) }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer { RoleViewModel(VinilosApplication.container.roleRepository) }
        }
    }
}
