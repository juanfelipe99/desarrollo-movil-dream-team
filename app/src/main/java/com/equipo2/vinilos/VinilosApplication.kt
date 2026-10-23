package com.equipo2.vinilos

import android.app.Application
import androidx.annotation.VisibleForTesting
import com.equipo2.vinilos.ui.thememode.applyThemeMode
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/** Arranque de la app: crea los repositorios y aplica el tema guardado. */
class VinilosApplication : Application() {

    private val appScope = MainScope()

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
        applySavedThemeMode()
    }

    private fun applySavedThemeMode() {
        val themeMode = container.themeModeRepository.themeMode

        // Antes de la primera pantalla, para no mostrar un instante el tema equivocado
        applyThemeMode(runBlocking { themeMode.first() })

        // Después se aplica cada vez que se guarda uno nuevo
        appScope.launch { themeMode.collect { applyThemeMode(it) } }
    }

    companion object {
        /** Repositorios de la app, que las pruebas pueden reemplazar por unos falsos. */
        lateinit var container: AppContainer
            @VisibleForTesting set
    }
}
