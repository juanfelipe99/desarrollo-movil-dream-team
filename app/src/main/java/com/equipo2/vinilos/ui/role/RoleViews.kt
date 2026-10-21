package com.equipo2.vinilos.ui.role

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import com.equipo2.vinilos.R
import com.equipo2.vinilos.data.model.Role
import com.equipo2.vinilos.databinding.ViewRoleChipBinding

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

/** Muestra el rol en el chip de la barra superior. Mientras no hay rol, el chip no se ve. */
fun ViewRoleChipBinding.show(role: Role?) {
    root.isVisible = role != null
    if (role == null) return
    val name = root.context.getString(role.nameRes)
    roleChipIcon.setImageResource(role.iconRes)
    roleChipText.text = name
    root.contentDescription = root.context.getString(R.string.cd_change_role, name)
}
