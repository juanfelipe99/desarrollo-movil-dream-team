package com.equipo2.vinilos.ui.artists.detail

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.equipo2.vinilos.R
import com.equipo2.vinilos.data.model.Artist

/** Detalle de artista (HU04). */
class ArtistDetailFragment : Fragment(R.layout.fragment_artist_detail) {

    companion object {
        const val ARG_ARTIST_ID = "artistId"

        // Músicos y bandas se consultan en rutas distintas de la API, por eso viaja también el tipo
        const val ARG_ARTIST_TYPE = "artistType"

        fun args(artist: Artist) = bundleOf(ARG_ARTIST_ID to artist.id, ARG_ARTIST_TYPE to artist.type.name)
    }
}
