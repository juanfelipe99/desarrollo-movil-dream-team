package com.equipo2.vinilos.ui.albums.list

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.equipo2.vinilos.R
import com.equipo2.vinilos.data.model.Role
import com.equipo2.vinilos.databinding.FragmentAlbumListBinding
import com.equipo2.vinilos.ui.common.collectWhileVisible
import com.equipo2.vinilos.ui.common.keepSnackbarsAbove
import com.equipo2.vinilos.ui.common.navigateIfPossible
import com.equipo2.vinilos.ui.role.RoleViewModel

/** Lista de álbumes (HU01). */
class AlbumListFragment : Fragment(R.layout.fragment_album_list) {

    private val roleViewModel: RoleViewModel by activityViewModels { RoleViewModel.Factory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentAlbumListBinding.bind(view)
        setUpCreateAlbumButton(binding)
    }

    /** Botón «+»: solo lo ve el coleccionista, abre Crear álbum y los avisos aparecen encima de él. */
    private fun setUpCreateAlbumButton(binding: FragmentAlbumListBinding) {
        val button = binding.createAlbumButton

        viewLifecycleOwner.collectWhileVisible(roleViewModel.role) { role -> button.isVisible = role == Role.COLLECTOR }

        button.setOnClickListener { findNavController().navigateIfPossible(R.id.action_albumList_to_albumCreate) }

        keepSnackbarsAbove(button)
    }
}
