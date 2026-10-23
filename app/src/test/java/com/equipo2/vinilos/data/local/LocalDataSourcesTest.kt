package com.equipo2.vinilos.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.equipo2.vinilos.data.model.Role
import com.equipo2.vinilos.data.model.ThemeMode
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class LocalDataSourcesTest {

    @get:Rule
    val folder = TemporaryFolder()

    private fun TestScope.dataStore(): DataStore<Preferences> =
        PreferenceDataStoreFactory.create(scope = backgroundScope) { File(folder.root, "prueba.preferences_pb") }

    @Test
    fun sinRolGuardado_elRolEsVisitante() = runTest {
        assertEquals(Role.VISITOR, RoleLocalDataSource(dataStore()).role.first())
    }

    @Test
    fun elRolGuardadoSeLeeDespues() = runTest {
        val source = RoleLocalDataSource(dataStore())

        source.save(Role.COLLECTOR)

        assertEquals(Role.COLLECTOR, source.role.first())
    }

    @Test
    fun unRolDesconocidoEnElArchivo_seLeeComoVisitante() = runTest {
        val store = dataStore()
        store.edit { it[stringPreferencesKey("role")] = "ADMIN" }

        assertEquals(Role.VISITOR, RoleLocalDataSource(store).role.first())
    }

    @Test
    fun sinTemaGuardado_elTemaEsElDelSistema() = runTest {
        assertEquals(ThemeMode.SYSTEM, ThemeModeLocalDataSource(dataStore()).themeMode.first())
    }

    @Test
    fun elTemaGuardadoSeLeeDespues() = runTest {
        val source = ThemeModeLocalDataSource(dataStore())

        source.save(ThemeMode.DARK)

        assertEquals(ThemeMode.DARK, source.themeMode.first())
    }
}
