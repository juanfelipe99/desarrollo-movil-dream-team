package com.equipo2.vinilos.ui.common

import androidx.annotation.IdRes
import androidx.navigation.NavController

/** Navega solo si la acción existe desde la pantalla actual. Devuelve si navegó. */
fun NavController.navigateIfPossible(@IdRes actionId: Int): Boolean {
    if (currentDestination?.getAction(actionId) == null) return false
    navigate(actionId)
    return true
}
