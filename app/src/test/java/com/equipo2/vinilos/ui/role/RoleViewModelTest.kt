package com.equipo2.vinilos.ui.role

import com.equipo2.vinilos.data.model.Role
import com.equipo2.vinilos.support.FakeRoleRepository
import com.equipo2.vinilos.support.MainDispatcherRule
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class RoleViewModelTest {

    @get:Rule
    val mainDispatcher = MainDispatcherRule()

    @Test
    fun alCrearse_leeElRolGuardado() {
        val viewModel = RoleViewModel(FakeRoleRepository(Role.COLLECTOR))

        assertEquals(Role.COLLECTOR, viewModel.role.value)
    }

    @Test
    fun cambiarRol_loCambiaYLoGuarda() {
        val repository = FakeRoleRepository(Role.VISITOR)
        val viewModel = RoleViewModel(repository)

        viewModel.changeRole(Role.COLLECTOR)

        assertEquals(Role.COLLECTOR, viewModel.role.value)
        assertEquals(Role.COLLECTOR, repository.role.value)
    }

    @Test
    fun siOtroLugarGuardaUnRol_elViewModelSeActualiza() {
        val repository = FakeRoleRepository(Role.VISITOR)
        val viewModel = RoleViewModel(repository)

        repository.role.value = Role.COLLECTOR

        assertEquals(Role.COLLECTOR, viewModel.role.value)
    }
}
