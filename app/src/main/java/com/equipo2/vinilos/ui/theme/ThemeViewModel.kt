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

        // Se guarda en segundo plano
        viewModelScope.launch { themeRepository.setThemeMode(mode) }

        // Se aplica sin esperar el guardado
        applyThemeMode(mode)
    }

    companion object {
        val Factory = viewModelFactory {
            initializer { ThemeViewModel(VinilosApplication.container.themeRepository) }
        }
    }
}
