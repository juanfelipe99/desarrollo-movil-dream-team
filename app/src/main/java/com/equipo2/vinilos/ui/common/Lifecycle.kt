package com.equipo2.vinilos.ui.common

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/** Escucha el flujo mientras la pantalla se ve, y se detiene sola al ocultarse o cerrarse. */
fun <T> LifecycleOwner.collectWhileVisible(flow: Flow<T>, action: suspend (T) -> Unit) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) { flow.collect(action) }
    }
}

/** Ejecuta la acción cuando termina el ciclo de vida, por ejemplo al cerrarse o recrearse la pantalla. */
fun Lifecycle.doOnDestroy(action: () -> Unit) {
    addObserver(object : DefaultLifecycleObserver {
        override fun onDestroy(owner: LifecycleOwner) = action()
    })
}
