package com.equipo2.vinilos.ui.albums.create

private val COVER_URL = Regex("""^https?://[^\s/]+\.\S+$""", RegexOption.IGNORE_CASE)

/** La URL de la portada es válida si empieza por http:// o https:// y sigue un dominio con punto. */
fun isValidCoverUrl(url: String): Boolean = COVER_URL.matches(url)
