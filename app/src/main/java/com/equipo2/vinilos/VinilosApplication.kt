package com.equipo2.vinilos

import android.app.Application
import com.equipo2.vinilos.ui.common.applyThemeMode
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class VinilosApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ServiceLocator.init(this)
        applySavedThemeMode()
    }

    /** Se aplica antes de la primera pantalla para que no aparezca un instante con el tema equivocado. */
    private fun applySavedThemeMode() {
        val mode = runBlocking { ServiceLocator.themeRepository.themeMode.first() }
        applyThemeMode(mode)
    }
}
