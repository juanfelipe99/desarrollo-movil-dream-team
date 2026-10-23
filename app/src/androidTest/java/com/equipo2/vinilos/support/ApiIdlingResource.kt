package com.equipo2.vinilos.support

import androidx.test.espresso.IdlingResource
import okhttp3.Dispatcher

/** Espresso espera mientras haya llamadas a la API en curso. */
class ApiIdlingResource(private val dispatcher: Dispatcher) : IdlingResource {

    @Volatile
    private var callback: IdlingResource.ResourceCallback? = null

    init {
        dispatcher.idleCallback = Runnable { callback?.onTransitionToIdle() }
    }

    override fun getName(): String = "API de Vinilos"

    override fun isIdleNow(): Boolean = dispatcher.runningCallsCount() == 0

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback) {
        this.callback = callback
    }
}
