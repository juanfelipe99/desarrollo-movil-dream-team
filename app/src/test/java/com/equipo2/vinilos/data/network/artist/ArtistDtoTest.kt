package com.equipo2.vinilos.data.network.artist

import com.equipo2.vinilos.data.model.Artist
import com.equipo2.vinilos.data.model.ArtistType
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class ArtistDtoTest {

    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun musico_seConvierteConSuFechaDeNacimiento() {
        val artist = MusicianDto(
            id = 100,
            name = "Rubén Blades Bellido de Luna",
            image = "https://ejemplo.com/ruben.jpg",
            birthDate = "1948-07-16T00:00:00.000Z",
        ).toArtist()

        assertEquals(
            Artist(100, "Rubén Blades Bellido de Luna", "https://ejemplo.com/ruben.jpg", ArtistType.MUSICIAN, LocalDate.of(1948, 7, 16)),
            artist,
        )
    }

    @Test
    fun banda_seConvierteConSuFechaDeCreacion() {
        val artist = BandDto(
            id = 101,
            name = "Queen",
            image = "https://ejemplo.com/queen.jpg",
            creationDate = "1970-01-01T00:00:00.000Z",
        ).toArtist()

        assertEquals(ArtistType.BAND, artist.type)
        assertEquals(LocalDate.of(1970, 1, 1), artist.date)
    }

    @Test
    fun respuestaDeLaApi_ignoraLosCamposQueNoSeUsan_yLaImagenEsOpcional() {
        val musician = json.decodeFromString<MusicianDto>(
            """{"id":4,"name":"Freddie Mercury","birthDate":"1946-09-05T00:00:00.000Z","description":"…","albums":[],"performerPrizes":[]}""",
        )

        assertEquals("", musician.image)
        assertEquals(1946, musician.toArtist().date.year)
    }
}
