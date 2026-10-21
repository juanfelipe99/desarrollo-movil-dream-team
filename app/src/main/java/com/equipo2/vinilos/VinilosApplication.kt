package com.equipo2.vinilos

import android.app.Application
import androidx.annotation.VisibleForTesting
import com.equipo2.vinilos.ui.common.applyThemeMode
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class VinilosApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
        applySavedThemeMode()
    }

    /** Se aplica antes de la primera pantalla para que no aparezca un instante con el tema equivocado. */
    private fun applySavedThemeMode() {
        val mode = runBlocking { container.themeRepository.themeMode.first() }
        applyThemeMode(mode)
    }

    companion object {
        /** Los repositorios de la app. Las pruebas lo cambian por uno con repositorios falsos. */
        lateinit var container: AppContainer
            @VisibleForTesting set
    }
}
