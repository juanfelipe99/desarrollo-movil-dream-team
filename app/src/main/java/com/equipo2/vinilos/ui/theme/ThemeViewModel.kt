package com.equipo2.vinilos.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.equipo2.vinilos.VinilosApplication
import com.equipo2.vinilos.data.model.ThemeMode
import com.equipo2.vinilos.data.repository.ThemeRepository
import kotlinx.coroutines.launch

class ThemeViewModel(private val themeRepository: ThemeRepository) : ViewModel() {

    /** Guarda el tema y lo aplica. */
    fun setDarkMode(enabled: Boolean) {
        val mode = if (enabled) ThemeMode.DARK else ThemeMode.LIGHT

        // Se guarda en segundo plano, y termina aunque la pantalla se recree por el cambio
        viewModelScope.launch { themeRepository.setThemeMode(mode) }

        // Se aplica de inmediato, sin esperar a que termine el guardado
        applyThemeMode(mode)
    }

    companion object {
        val Factory = viewModelFactory {
            initializer { ThemeViewModel(VinilosApplication.container.themeRepository) }
        }
    }
}
