package com.equipo2.vinilos.ui.albums.create

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate

/** Datos del formulario de Crear álbum que no guardan los campos de texto, y se conservan al girar la pantalla. */
class AlbumCreateViewModel : ViewModel() {

    private val currentReleaseDate = MutableStateFlow<LocalDate?>(null)

    val releaseDate: StateFlow<LocalDate?> = currentReleaseDate

    /** Guarda la fecha elegida en el selector. */
    fun setReleaseDate(date: LocalDate) {
        currentReleaseDate.value = date
    }
}
