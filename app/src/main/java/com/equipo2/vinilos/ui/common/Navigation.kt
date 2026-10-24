package com.equipo2.vinilos.ui.common

import android.os.Bundle
import androidx.activity.addCallback
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController

/** Navega solo si la acción existe desde la pantalla actual, con args si la pantalla los pide, y devuelve si navegó. */
fun NavController.navigateIfPossible(@IdRes actionId: Int, args: Bundle? = null): Boolean {
    // Si la acción no existe desde aquí, ya se navegó con un toque anterior
    if (currentDestination?.getAction(actionId) == null) return false
    navigate(actionId, args)
    return true
}

/** Atrás cierra la app mientras se vea esta pantalla. */
fun Fragment.finishAppOnBack() {
    requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) { requireActivity().finish() }
}
