package com.equipo2.vinilos.data.repository

import com.equipo2.vinilos.data.local.RolePreferences
import com.equipo2.vinilos.data.model.Role
import kotlinx.coroutines.flow.Flow

interface RoleRepository {
    /** El rol guardado. Visitante si no hay ninguno. */
    val role: Flow<Role>
    suspend fun setRole(role: Role)
}

class RoleRepositoryImpl(private val local: RolePreferences) : RoleRepository {

    override val role = local.role

    override suspend fun setRole(role: Role) = local.save(role)
}
