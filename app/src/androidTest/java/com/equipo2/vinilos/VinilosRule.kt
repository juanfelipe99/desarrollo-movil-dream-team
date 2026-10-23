package com.equipo2.vinilos

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.IdlingPolicies
import androidx.test.espresso.IdlingRegistry
import com.equipo2.vinilos.data.model.ThemeMode
import kotlinx.coroutines.runBlocking
import org.junit.rules.ExternalResource
import java.util.concurrent.TimeUnit

/** Prepara cada prueba E2E: espera a la API, abre la app y al final deja el tema y los repositorios como estaban. */
class VinilosRule : ExternalResource() {

    private val originalContainer = VinilosApplication.container
    private val apiIdling = ApiIdlingResource(originalContainer.apiClient.httpClient.dispatcher)
    private var scenario: ActivityScenario<MainActivity>? = null

    override fun before() {
        // Render puede tardar casi un minuto en despertar, más que la espera por defecto de Espresso
        IdlingPolicies.setIdlingResourceTimeout(2, TimeUnit.MINUTES)
        IdlingPolicies.setMasterPolicyTimeout(2, TimeUnit.MINUTES)
        IdlingRegistry.getInstance().register(apiIdling)
        BackVynils.wakeUp()
    }

    override fun after() {
        scenario?.close()
        IdlingRegistry.getInstance().unregister(apiIdling)
        VinilosApplication.container = originalContainer
        runBlocking { originalContainer.themeModeRepository.setThemeMode(ThemeMode.SYSTEM) }
    }

    /** Abre la app, que siempre empieza en la selección de rol. */
    fun launch() {
        scenario = ActivityScenario.launch(MainActivity::class.java)
    }

    /** Estado de la actividad, por ejemplo para comprobar que Atrás cerró la app. */
    val state: Lifecycle.State
        get() = checkNotNull(scenario).state
}
