package com.equipo2.vinilos.ui.common

import androidx.annotation.IdRes
import androidx.navigation.NavController

/** Navega solo si la acción existe desde la pantalla actual. Devuelve si navegó. */
fun NavController.navigateIfPossible(@IdRes actionId: Int): Boolean {
    // Si la acción no existe desde aquí, probablemente ya se navegó con el primer toque
    if (currentDestination?.getAction(actionId) == null) return false

    navigate(actionId)
    return true
}
