package com.equipo2.vinilos.ui.thememode

import com.equipo2.vinilos.data.model.ThemeMode
import com.equipo2.vinilos.support.FakeThemeModeRepository
import com.equipo2.vinilos.support.MainDispatcherRule
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class ThemeModeViewModelTest {

    @get:Rule
    val mainDispatcher = MainDispatcherRule()

    @Test
    fun alCrearse_leeElTemaGuardado() {
        val viewModel = ThemeModeViewModel(FakeThemeModeRepository(ThemeMode.DARK))

        assertEquals(ThemeMode.DARK, viewModel.themeMode.value)
    }

    @Test
    fun cambiarTema_loCambiaYLoGuarda() {
        val repository = FakeThemeModeRepository(ThemeMode.SYSTEM)
        val viewModel = ThemeModeViewModel(repository)

        viewModel.setThemeMode(ThemeMode.LIGHT)

        assertEquals(ThemeMode.LIGHT, viewModel.themeMode.value)
        assertEquals(ThemeMode.LIGHT, repository.themeMode.value)
    }
}
