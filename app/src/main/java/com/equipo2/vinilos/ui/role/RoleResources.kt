package com.equipo2.vinilos.ui.role

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.equipo2.vinilos.R
import com.equipo2.vinilos.data.model.Role

@get:StringRes
val Role.nameRes: Int
    get() = when (this) {
        Role.VISITOR -> R.string.role_visitor
        Role.COLLECTOR -> R.string.role_collector
    }

@get:DrawableRes
val Role.iconRes: Int
    get() = when (this) {
        Role.VISITOR -> R.drawable.ic_person
        Role.COLLECTOR -> R.drawable.ic_album
    }

/** Aviso al cambiar a este rol: «Ahora estás en modo …». */
@get:StringRes
val Role.changedMessageRes: Int
    get() = when (this) {
        Role.VISITOR -> R.string.snackbar_role_visitor
        Role.COLLECTOR -> R.string.snackbar_role_collector
    }
