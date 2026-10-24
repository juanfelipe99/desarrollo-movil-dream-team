package com.equipo2.vinilos.ui.albums.list

import com.equipo2.vinilos.data.model.Album
import com.equipo2.vinilos.data.repository.DataError
import com.equipo2.vinilos.support.FakeAlbumRepository
import com.equipo2.vinilos.support.MainDispatcherRule
import com.equipo2.vinilos.ui.state.UiState
import kotlinx.coroutines.CompletableDeferred
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

class AlbumListViewModelTest {

    @get:Rule
    val mainDispatcher = MainDispatcherRule()

    private val repository = FakeAlbumRepository()

    /** Hace que el catálogo espere hasta que la prueba lo complete. */
    private fun pendingResponse(): CompletableDeferred<Result<List<Album>>> {
        val response = CompletableDeferred<Result<List<Album>>>()
        repository.albums = { response.await() }
        return response
    }

    @Test
    fun alAbrir_quedaCargandoHastaQueLaApiResponda() {
        pendingResponse()

        val viewModel = AlbumListViewModel(repository)

        assertEquals(UiState.Loading, viewModel.state.value)
        assertEquals(1, repository.albumRequests)
    }

    @Test
    fun siHayAlbumes_muestraElCatalogo() {
        val albums = listOf(album(100), album(101))
        repository.albums = { Result.success(albums) }

        val viewModel = AlbumListViewModel(repository)

        assertEquals(UiState.Success(albums), viewModel.state.value)
    }

    @Test
    fun siNoHayAlbumes_quedaVacio() {
        repository.albums = { Result.success(emptyList()) }

        val viewModel = AlbumListViewModel(repository)

        assertEquals(UiState.Empty, viewModel.state.value)
    }

    @Test
    fun siLaApiFalla_quedaEnError() {
        repository.albums = { Result.failure(DataError.Network()) }

        val viewModel = AlbumListViewModel(repository)

        assertEquals(UiState.Error, viewModel.state.value)
    }

    @Test
    fun alReintentar_vuelveACargarYMuestraElCatalogo() {
        repository.albums = { Result.failure(DataError.Server(500)) }
        val viewModel = AlbumListViewModel(repository)

        val response = pendingResponse()
        viewModel.retry()

        assertEquals(UiState.Loading, viewModel.state.value)
        assertEquals(2, repository.albumRequests)

        val albums = listOf(album(100))
        response.complete(Result.success(albums))

        assertEquals(UiState.Success(albums), viewModel.state.value)
    }

    @Test
    fun siElReintentoTambienFalla_vuelveAError() {
        repository.albums = { Result.failure(DataError.Timeout()) }
        val viewModel = AlbumListViewModel(repository)

        viewModel.retry()

        assertEquals(UiState.Error, viewModel.state.value)
        assertEquals(2, repository.albumRequests)
    }

    @Test
    fun reintentarMientrasCarga_noRepiteLaPeticion() {
        pendingResponse()
        val viewModel = AlbumListViewModel(repository)

        viewModel.retry()
        viewModel.retry()

        assertEquals(1, repository.albumRequests)
    }

    private fun album(id: Int) = Album(
        id = id,
        name = "Buscando América",
        cover = "https://ejemplo.com/portada.jpg",
        releaseDate = LocalDate.of(1984, 8, 1),
        description = "Álbum de Rubén Blades.",
        genre = "Salsa",
        recordLabel = "Elektra",
    )
}
