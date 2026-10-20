package com.equipo2.vinilos.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.equipo2.vinilos.data.local.appPreferences
import com.equipo2.vinilos.data.model.Role
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface RoleRepository {
    /** El rol elegido, o null si todavía no se eligió ninguno. */
    val role: Flow<Role?>
    suspend fun setRole(role: Role)
}

class RoleRepositoryImpl(context: Context) : RoleRepository {

    private val preferences = context.appPreferences

    override val role = preferences.data.map { prefs ->
        Role.entries.find { it.name == prefs[ROLE_KEY] }
    }

    override suspend fun setRole(role: Role) {
        preferences.edit { it[ROLE_KEY] = role.name }
    }
}

private val ROLE_KEY = stringPreferencesKey("role")
