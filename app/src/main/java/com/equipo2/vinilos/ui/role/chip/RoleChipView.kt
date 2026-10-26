package com.equipo2.vinilos.ui.role.chip

import androidx.core.view.isVisible
import com.equipo2.vinilos.R
import com.equipo2.vinilos.data.model.Role
import com.equipo2.vinilos.databinding.ViewRoleChipBinding
import com.equipo2.vinilos.ui.role.iconRes
import com.equipo2.vinilos.ui.role.nameRes

/** Muestra el rol en el chip: ícono, nombre y lo que lee TalkBack. */
fun ViewRoleChipBinding.show(role: Role?) {
    // Mientras se lee el rol no hay nada que mostrar
    root.isVisible = role != null
    if (role == null) return

    val context = root.context
    val name = context.getString(role.nameRes)

    roleChipIcon.setImageResource(role.iconRes)
    roleChipText.text = name
    root.contentDescription = context.getString(R.string.cd_change_role, name)
}
