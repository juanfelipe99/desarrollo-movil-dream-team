package com.equipo2.vinilos.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.equipo2.vinilos.data.model.Role
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/** Guarda el rol en las preferencias del teléfono, como texto con el nombre del rol. */
class RolePreferences(private val dataStore: DataStore<Preferences>) {

    /** Sin rol guardado, o con un texto que no es un rol, es visitante. */
    val role: Flow<Role> = dataStore.data.map { prefs ->
        Role.entries.find { it.name == prefs[ROLE_KEY] } ?: Role.VISITOR
    }

    suspend fun save(role: Role) {
        dataStore.edit { it[ROLE_KEY] = role.name }
    }
}

private val ROLE_KEY = stringPreferencesKey("role")
