package com.equipo2.vinilos.ui.artists.detail

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.equipo2.vinilos.R
import com.equipo2.vinilos.data.model.Artist
import com.equipo2.vinilos.databinding.FragmentArtistDetailBinding
import com.equipo2.vinilos.ui.state.showEmpty

/** Detalle de artista (HU04). Hasta el sprint 2 solo avisa que llegará en la próxima versión. */
class ArtistDetailFragment : Fragment(R.layout.fragment_artist_detail) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        FragmentArtistDetailBinding.bind(view).artistSoon.showEmpty(
            getString(R.string.coming_soon_title),
            getString(R.string.artist_detail_soon_body),
            R.drawable.ic_mic,
        )
    }

    companion object {
        const val ARG_ARTIST_ID = "artistId"

        // Músicos y bandas se consultan en rutas distintas de la API, por eso viaja también el tipo
        const val ARG_ARTIST_TYPE = "artistType"

        fun args(artist: Artist) = bundleOf(ARG_ARTIST_ID to artist.id, ARG_ARTIST_TYPE to artist.type.name)
    }
}
