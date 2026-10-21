package com.equipo2.vinilos.ui.albums.list

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.equipo2.vinilos.MainActivity
import com.equipo2.vinilos.R
import com.equipo2.vinilos.data.model.Role
import com.equipo2.vinilos.databinding.FragmentAlbumListBinding
import com.equipo2.vinilos.ui.common.navigateIfPossible
import com.equipo2.vinilos.ui.role.RoleViewModel
import kotlinx.coroutines.launch

class AlbumListFragment : Fragment(R.layout.fragment_album_list) {

    private val roleViewModel: RoleViewModel by activityViewModels { RoleViewModel.Factory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentAlbumListBinding.bind(view)
        setUpCreateAlbumButton(binding)
    }

    override fun onDestroyView() {
        (requireActivity() as MainActivity).snackbarAnchor = null
        super.onDestroyView()
    }

    /** Botón «+» solo lo ve el coleccionista y abre Crear álbum. */
    private fun setUpCreateAlbumButton(binding: FragmentAlbumListBinding) {
        val button = binding.createAlbumButton

        // Aparece o desaparece cada vez que cambia el rol
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                roleViewModel.role.collect { role -> button.isVisible = role == Role.COLLECTOR }
            }
        }

        button.setOnClickListener { findNavController().navigateIfPossible(R.id.action_albumList_to_albumCreate) }

        // Los avisos aparecen encima del botón y no lo tapan
        (requireActivity() as MainActivity).snackbarAnchor = button
    }
}
