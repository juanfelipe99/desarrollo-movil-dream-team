package com.equipo2.vinilos.ui.albums.create

import androidx.annotation.StringRes
import com.equipo2.vinilos.R
import com.equipo2.vinilos.data.model.AlbumCatalog
import com.equipo2.vinilos.data.model.NewAlbum
import java.time.LocalDate

/** Lo escrito y elegido en el formulario de Crear álbum. */
data class AlbumForm(
    val name: String = "",
    val cover: String = "",
    val releaseDate: LocalDate? = null,
    val genre: String? = null,
    val recordLabel: String? = null,
    val description: String = "",
)

/** Campos del formulario en el orden en que aparecen en pantalla. */
enum class AlbumField { NAME, COVER, RELEASE_DATE, GENRE, RECORD_LABEL, DESCRIPTION }

private val COVER_URL = Regex("""^https?://[^\s/]+\.\S+$""", RegexOption.IGNORE_CASE)

/** Válida si empieza por http:// o https:// y sigue un dominio con punto. */
fun isValidCoverUrl(url: String): Boolean = COVER_URL.matches(url.trim())

/** Mensaje de error de cada campo que no cumple su regla, en el orden de la pantalla. */
fun AlbumForm.errors(): Map<AlbumField, Int> = buildMap {
    if (name.isBlank()) put(AlbumField.NAME, R.string.error_album_name_required)
    coverError()?.let { put(AlbumField.COVER, it) }
    if (releaseDate == null) put(AlbumField.RELEASE_DATE, R.string.error_release_date_required)
    if (genre !in AlbumCatalog.genres) put(AlbumField.GENRE, R.string.error_genre_required)
    if (recordLabel !in AlbumCatalog.recordLabels) put(AlbumField.RECORD_LABEL, R.string.error_record_label_required)
    if (description.isBlank()) put(AlbumField.DESCRIPTION, R.string.error_description_required)
}

@StringRes
private fun AlbumForm.coverError(): Int? = when {
    cover.isBlank() -> R.string.error_cover_required
    !isValidCoverUrl(cover) -> R.string.error_cover_invalid
    else -> null
}

/** Álbum para la API, que solo se pide con el formulario válido. */
fun AlbumForm.toNewAlbum() = NewAlbum(
    name = name.trim(),
    cover = cover.trim(),
    releaseDate = checkNotNull(releaseDate),
    description = description.trim(),
    genre = checkNotNull(genre),
    recordLabel = checkNotNull(recordLabel),
)
