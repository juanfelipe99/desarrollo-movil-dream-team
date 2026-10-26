package com.equipo2.vinilos.ui.albums.create

import com.equipo2.vinilos.R
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class AlbumFormTest {

    private val valid = AlbumForm(
        name = "Amor y control",
        cover = "https://ejemplo.com/portadas/amor-y-control.jpg",
        releaseDate = LocalDate.of(1992, 6, 1),
        genre = "Salsa",
        recordLabel = "Sony Music",
        description = "Álbum de Rubén Blades publicado en 1992.",
    )

    @Test
    fun formularioVacio_tieneUnErrorPorCampo_enElOrdenDeLaPantalla() {
        val errors = AlbumForm().errors()

        assertEquals(AlbumField.entries, errors.keys.toList())
        assertEquals(R.string.error_album_name_required, errors[AlbumField.NAME])
        assertEquals(R.string.error_cover_required, errors[AlbumField.COVER])
        assertEquals(R.string.error_release_date_required, errors[AlbumField.RELEASE_DATE])
        assertEquals(R.string.error_genre_required, errors[AlbumField.GENRE])
        assertEquals(R.string.error_record_label_required, errors[AlbumField.RECORD_LABEL])
        assertEquals(R.string.error_description_required, errors[AlbumField.DESCRIPTION])
    }

    @Test
    fun formularioCompleto_noTieneErrores() {
        assertTrue(valid.errors().isEmpty())
    }

    @Test
    fun nombreYDescripcionConSoloEspacios_cuentanComoVacios() {
        val errors = valid.copy(name = "   ", description = "\n ").errors()

        assertEquals(R.string.error_album_name_required, errors[AlbumField.NAME])
        assertEquals(R.string.error_description_required, errors[AlbumField.DESCRIPTION])
    }

    @Test
    fun urlSinHttp_muestraElErrorDeFormato() {
        val errors = valid.copy(cover = "portada-amor-y-control").errors()

        assertEquals(mapOf(AlbumField.COVER to R.string.error_cover_invalid), errors)
    }

    @Test
    fun urlsValidas() {
        listOf(
            "http://ejemplo.com/portada.jpg",
            "https://ejemplo.com/portada.jpg",
            "HTTPS://EJEMPLO.COM/PORTADA.JPG",
            "  https://ejemplo.com/portada.jpg  ",
            "https://upload.wikimedia.org/wikipedia/en/2/2e/Amor_y_control.jpg",
        ).forEach { url -> assertTrue(url, isValidCoverUrl(url)) }
    }

    @Test
    fun urlsInvalidas() {
        listOf(
            "",
            "ejemplo.com/portada.jpg",
            "ftp://ejemplo.com/portada.jpg",
            "https://localhost/portada.jpg",
            "https://ejemplo .com/portada.jpg",
            "https://",
        ).forEach { url -> assertFalse(url, isValidCoverUrl(url)) }
    }

    @Test
    fun generoOSelloFueraDelCatalogoDeLaApi_sonErrores() {
        val errors = valid.copy(genre = "Pop", recordLabel = "Universal").errors()

        assertEquals(R.string.error_genre_required, errors[AlbumField.GENRE])
        assertEquals(R.string.error_record_label_required, errors[AlbumField.RECORD_LABEL])
    }

    @Test
    fun toNewAlbum_quitaLosEspaciosDeLosTextos() {
        val album = valid.copy(name = "  Amor y control ", cover = " ${valid.cover} ", description = " Salsa ").toNewAlbum()

        assertEquals("Amor y control", album.name)
        assertEquals(valid.cover, album.cover)
        assertEquals("Salsa", album.description)
        assertEquals(LocalDate.of(1992, 6, 1), album.releaseDate)
        assertEquals("Salsa", album.genre)
        assertEquals("Sony Music", album.recordLabel)
    }
}
