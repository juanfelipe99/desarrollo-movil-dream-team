package com.equipo2.vinilos.ui.thememode

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.equipo2.vinilos.VinilosApplication
import com.equipo2.vinilos.data.model.ThemeMode
import com.equipo2.vinilos.data.repository.ThemeModeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/** Tema elegido, compartido por la selección de rol y el menú de rol. */
class ThemeModeViewModel(private val repository: ThemeModeRepository) : ViewModel() {

    private val currentMode = MutableStateFlow<ThemeMode?>(null)

    val themeMode: StateFlow<ThemeMode?> = currentMode

    init {
        viewModelScope.launch { repository.themeMode.collect { currentMode.value = it } }
    }

    /** Cambia el tema y lo guarda, y la app lo aplica al guardarse. */
    fun setThemeMode(themeMode: ThemeMode) {
        currentMode.value = themeMode
        viewModelScope.launch { repository.setThemeMode(themeMode) }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer { ThemeModeViewModel(VinilosApplication.container.themeModeRepository) }
        }
    }
}
