package com.equipo2.vinilos.support

import com.equipo2.vinilos.data.model.Artist
import com.equipo2.vinilos.data.model.ArtistType
import com.equipo2.vinilos.data.repository.ArtistRepository
import kotlinx.coroutines.CompletableDeferred
import java.time.LocalDate

/** Cuenta las consultas y responde lo que la prueba le indique. */
class FakeArtistRepository : ArtistRepository {

    var calls = 0
        private set

    /** Respuesta de la próxima consulta, que la prueba completa cuando quiera. */
    var response = CompletableDeferred<Result<List<Artist>>>()

    override suspend fun getArtists(): Result<List<Artist>> {
        calls++
        return response.await()
    }
}

fun musician(id: Int, name: String, birthYear: Int = 1950) =
    Artist(id, name, image = "", type = ArtistType.MUSICIAN, date = LocalDate.of(birthYear, 1, 1))

fun band(id: Int, name: String, creationYear: Int = 1970) =
    Artist(id, name, image = "", type = ArtistType.BAND, date = LocalDate.of(creationYear, 1, 1))
