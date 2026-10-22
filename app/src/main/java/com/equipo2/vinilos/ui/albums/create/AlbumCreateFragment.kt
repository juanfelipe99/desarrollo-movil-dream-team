package com.equipo2.vinilos.ui.albums.create

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.equipo2.vinilos.R
import com.equipo2.vinilos.databinding.FragmentAlbumCreateBinding

/** Formulario para crear un álbum (HU07). */
class AlbumCreateFragment : Fragment(R.layout.fragment_album_create) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentAlbumCreateBinding.bind(view)

        // Cancelar cierra la pantalla igual que la X
        binding.cancelButton.setOnClickListener { findNavController().navigateUp() }
    }
}
