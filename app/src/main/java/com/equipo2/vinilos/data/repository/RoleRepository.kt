package com.equipo2.vinilos.data.repository

import com.equipo2.vinilos.data.local.RoleLocalDataSource
import com.equipo2.vinilos.data.model.Role
import kotlinx.coroutines.flow.Flow

interface RoleRepository {
    val role: Flow<Role>
    suspend fun setRole(role: Role)
}

class RoleRepositoryImpl(private val localDataSource: RoleLocalDataSource) : RoleRepository {

    override val role = localDataSource.role

    override suspend fun setRole(role: Role) = localDataSource.save(role)
}
