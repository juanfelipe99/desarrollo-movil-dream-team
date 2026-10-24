package com.equipo2.vinilos.support

import com.equipo2.vinilos.data.model.Album
import com.equipo2.vinilos.data.model.NewAlbum
import com.equipo2.vinilos.data.model.Role
import com.equipo2.vinilos.data.model.ThemeMode
import com.equipo2.vinilos.data.repository.AlbumRepository
import com.equipo2.vinilos.data.repository.RoleRepository
import com.equipo2.vinilos.data.repository.ThemeModeRepository
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.MutableStateFlow

/** Guarda lo que recibe y responde lo que la prueba le indique. */
class FakeAlbumRepository : AlbumRepository {

    val created = mutableListOf<NewAlbum>()

    /** Respuesta de la próxima creación, que la prueba completa cuando quiera. */
    var response = CompletableDeferred<Result<Album>>()

    override suspend fun createAlbum(album: NewAlbum): Result<Album> {
        created += album
        return response.await()
    }

    /** Cuántas veces se pidió el catálogo. */
    var albumRequests = 0
        private set

    /** Respuesta del catálogo; puede esperar un CompletableDeferred para probar la carga. */
    var albums: suspend () -> Result<List<Album>> = { Result.success(emptyList()) }

    override suspend fun getAlbums(): Result<List<Album>> {
        albumRequests++
        return albums()
    }

    override suspend fun getAlbumById(id: Int): Result<Album> = Result.failure(UnsupportedOperationException())
}

/** Repositorio de rol en memoria. */
class FakeRoleRepository(initial: Role) : RoleRepository {

    override val role = MutableStateFlow(initial)

    override suspend fun setRole(role: Role) {
        this.role.value = role
    }
}

/** Repositorio de tema en memoria. */
class FakeThemeModeRepository(initial: ThemeMode) : ThemeModeRepository {

    override val themeMode = MutableStateFlow(initial)

    override suspend fun setThemeMode(mode: ThemeMode) {
        themeMode.value = mode
    }
}
