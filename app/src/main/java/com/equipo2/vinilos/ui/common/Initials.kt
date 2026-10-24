package com.equipo2.vinilos.ui.common

/** Primera letra de las dos primeras palabras, por ejemplo «RB» para Rubén Blades o «Q» para Queen. */
fun initialsOf(name: String): String =
    name.split(' ').filter { it.isNotBlank() }.take(2).joinToString("") { it.first().uppercase() }
