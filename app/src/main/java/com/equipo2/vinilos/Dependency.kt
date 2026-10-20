package com.equipo2.vinilos

import androidx.annotation.VisibleForTesting
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/** Como by lazy: crea el valor la primera vez que se pide. La diferencia es que las pruebas pueden reemplazarlo por uno falso. */
fun <T : Any> dependency(create: () -> T): ReadWriteProperty<Any?, T> = Dependency(create).also { all += it }

/** Borra los valores creados o reemplazados, así el siguiente uso vuelve a crear los reales. Cada prueba lo llama al terminar. */
@VisibleForTesting
fun resetDependencies() {
    all.forEach { it.reset() }
}

private val all = mutableListOf<Dependency<*>>()

private class Dependency<T : Any>(private val create: () -> T) : ReadWriteProperty<Any?, T> {

    private var value: T? = null

    @Synchronized
    override fun getValue(thisRef: Any?, property: KProperty<*>): T = value ?: create().also { value = it }

    @Synchronized
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = value
    }

    @Synchronized
    fun reset() {
        value = null
    }
}
