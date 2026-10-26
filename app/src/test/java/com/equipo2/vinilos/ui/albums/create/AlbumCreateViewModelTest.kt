package com.equipo2.vinilos.ui.albums.create

import com.equipo2.vinilos.R
import com.equipo2.vinilos.data.model.Album
import com.equipo2.vinilos.support.FakeAlbumRepository
import com.equipo2.vinilos.support.MainDispatcherRule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.io.IOException
import java.time.LocalDate

class AlbumCreateViewModelTest {

    @get:Rule
    val mainDispatcher = MainDispatcherRule()

    private val repository = FakeAlbumRepository()
    private val viewModel = AlbumCreateViewModel(repository)

    private fun fillValidForm() = viewModel.update {
        copy(
            name = "Amor y control",
            cover = "https://ejemplo.com/portada.jpg",
            releaseDate = LocalDate.of(1992, 6, 1),
            genre = "Salsa",
            recordLabel = "Sony Music",
            description = "Álbum de Rubén Blades.",
        )
    }

    @Test
    fun antesDeGuardar_noHayErroresAunqueElFormularioEsteVacio() {
        viewModel.update { copy(name = "") }

        assertTrue(viewModel.errors.value.isEmpty())
    }

    @Test
    fun guardarVacio_muestraLosErrores_yNoLlamaALaApi() {
        val saved = viewModel.save()

        assertFalse(saved)
        assertEquals(AlbumField.entries, viewModel.errors.value.keys.toList())
        assertTrue(repository.created.isEmpty())
        assertFalse(viewModel.isSaving.value)
    }

    @Test
    fun despuesDeGuardar_cadaCambioActualizaLosErrores() {
        viewModel.save()

        viewModel.update { copy(name = "Amor y control") }
        assertFalse(AlbumField.NAME in viewModel.errors.value)

        viewModel.update { copy(cover = "portada") }
        assertEquals(R.string.error_cover_invalid, viewModel.errors.value[AlbumField.COVER])
    }

    @Test
    fun guardarValido_quedaGuardandoHastaQueLaApiResponda() {
        fillValidForm()

        assertTrue(viewModel.save())

        assertTrue(viewModel.isSaving.value)
        assertEquals("Amor y control", repository.created.single().name)
    }

    @Test
    fun siLaApiCreaElAlbum_avisaConSuId() = runTest {
        fillValidForm()
        viewModel.save()

        repository.response.complete(Result.success(album(id = 101)))

        assertEquals(AlbumCreateEvent.Saved(101), viewModel.events.first())
        assertFalse(viewModel.isSaving.value)
    }

    @Test
    fun siLaApiFalla_avisaYConservaElFormulario() = runTest {
        fillValidForm()
        viewModel.save()

        repository.response.complete(Result.failure(IOException("Sin conexión")))

        assertEquals(AlbumCreateEvent.Failed, viewModel.events.first())
        assertFalse(viewModel.isSaving.value)
        assertEquals("Amor y control", viewModel.form.value.name)
    }

    private fun album(id: Int) = Album(
        id = id,
        name = "Amor y control",
        cover = "https://ejemplo.com/portada.jpg",
        releaseDate = LocalDate.of(1992, 6, 1),
        description = "Álbum de Rubén Blades.",
        genre = "Salsa",
        recordLabel = "Sony Music",
    )
}
