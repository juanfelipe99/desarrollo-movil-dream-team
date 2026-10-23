package com.equipo2.vinilos.data.network.album

import com.equipo2.vinilos.data.model.NewAlbum
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class AlbumDtoTest {

    @Test
    fun toRequest_enviaLaFechaEnElFormatoDeLaApi() {
        val request = NewAlbum(
            name = "Amor y control",
            cover = "https://ejemplo.com/portada.jpg",
            releaseDate = LocalDate.of(1992, 6, 1),
            description = "Álbum de Rubén Blades.",
            genre = "Salsa",
            recordLabel = "Sony Music",
        ).toRequest()

        assertEquals(
            CreateAlbumRequest(
                name = "Amor y control",
                cover = "https://ejemplo.com/portada.jpg",
                releaseDate = "1992-06-01T00:00:00.000Z",
                description = "Álbum de Rubén Blades.",
                genre = "Salsa",
                recordLabel = "Sony Music",
            ),
            request,
        )
    }

    @Test
    fun toAlbum_convierteLaFechaDeLaApi() {
        val album = AlbumDto(
            id = 101,
            name = "Amor y control",
            cover = "https://ejemplo.com/portada.jpg",
            releaseDate = "1992-06-01T00:00:00.000Z",
            description = "Álbum de Rubén Blades.",
            genre = "Salsa",
            recordLabel = "Sony Music",
        ).toAlbum()

        assertEquals(101, album.id)
        assertEquals(LocalDate.of(1992, 6, 1), album.releaseDate)
        assertEquals("Sony Music", album.recordLabel)
    }
}
