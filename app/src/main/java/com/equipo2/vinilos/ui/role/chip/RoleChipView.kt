package com.equipo2.vinilos.ui.role.chip

import androidx.core.view.isVisible
import com.equipo2.vinilos.R
import com.equipo2.vinilos.data.model.Role
import com.equipo2.vinilos.databinding.ViewRoleChipBinding
import com.equipo2.vinilos.ui.role.iconRes
import com.equipo2.vinilos.ui.role.nameRes

/** Muestra el rol en el chip. Mientras se lee, el chip no se ve. */
fun ViewRoleChipBinding.show(role: Role?) {
    root.isVisible = role != null
    if (role == null) return

    // Ícono del rol: persona o disco
    roleChipIcon.setImageResource(role.iconRes)

    // Nombre del rol y lo que lee TalkBack: «Cambiar de rol. Modo actual: …»
    val name = root.context.getString(role.nameRes)
    roleChipText.text = name
    root.contentDescription = root.context.getString(R.string.cd_change_role, name)
}
