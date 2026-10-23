package com.equipo2.vinilos

import androidx.test.espresso.IdlingResource
import okhttp3.Dispatcher

/** Hace que Espresso espere mientras haya llamadas a la API en curso, sin pausas fijas. */
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
