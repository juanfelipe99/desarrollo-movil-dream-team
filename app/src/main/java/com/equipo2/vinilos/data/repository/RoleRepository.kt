package com.equipo2.vinilos.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.equipo2.vinilos.data.model.Role
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface RoleRepository {
    /** El rol elegido, o null si todavía no se eligió ninguno. */
    val role: Flow<Role?>
    suspend fun setRole(role: Role)
    suspend fun clearRole()
}

class RoleRepositoryImpl(private val preferences: DataStore<Preferences>) : RoleRepository {

    override val role = preferences.data.map { prefs ->
        Role.entries.find { it.name == prefs[ROLE_KEY] }
    }

    override suspend fun setRole(role: Role) {
        preferences.edit { it[ROLE_KEY] = role.name }
    }

    override suspend fun clearRole() {
        preferences.edit { it.remove(ROLE_KEY) }
    }
}

private val ROLE_KEY = stringPreferencesKey("role")
