package com.equipo2.vinilos.data.network.album

import com.equipo2.vinilos.data.model.NewAlbum
import kotlinx.serialization.json.Json
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

    @Test
    fun listaDeLaApi_seLeeYConvierteCadaAlbum() {
        val body = """
            [
              {"id": 100, "name": "Buscando América", "cover": "https://ejemplo.com/a.jpg",
               "releaseDate": "1984-08-01T00:00:00.000Z", "description": "Rubén Blades",
               "genre": "Salsa", "recordLabel": "Elektra",
               "tracks": [{"id": 1, "name": "Decisiones", "duration": "5:05"}],
               "performers": [], "comments": [], "campoExtra": true},
              {"id": 101, "name": "Poeta del pueblo", "cover": "https://ejemplo.com/b.jpg",
               "releaseDate": "1984-08-01T00:00:00.000Z", "description": "Rubén Blades",
               "genre": "Salsa", "recordLabel": "Elektra"}
            ]
        """.trimIndent()

        val albums = Json { ignoreUnknownKeys = true }
            .decodeFromString<List<AlbumDto>>(body)
            .map { it.toAlbum() }

        assertEquals(listOf(100, 101), albums.map { it.id })
        assertEquals("Decisiones", albums[0].tracks.single().name)
        assertEquals(emptyList<Any>(), albums[1].tracks)
        assertEquals(LocalDate.of(1984, 8, 1), albums[1].releaseDate)
    }
}
