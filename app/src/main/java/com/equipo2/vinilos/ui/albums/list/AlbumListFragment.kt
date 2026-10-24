package com.equipo2.vinilos.ui.albums.list

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.equipo2.vinilos.R
import com.equipo2.vinilos.data.model.Role
import com.equipo2.vinilos.databinding.FragmentAlbumListBinding
import com.equipo2.vinilos.ui.albums.create.AlbumCreateFragment
import com.equipo2.vinilos.ui.common.collectWhileVisible
import com.equipo2.vinilos.ui.common.keepSnackbarsAbove
import com.equipo2.vinilos.ui.common.navigateIfPossible
import com.equipo2.vinilos.ui.common.showSnackbar
import com.equipo2.vinilos.ui.role.RoleViewModel
import com.equipo2.vinilos.ui.state.UiState
import com.equipo2.vinilos.ui.state.showEmpty
import com.equipo2.vinilos.ui.state.showError
import com.equipo2.vinilos.ui.state.showLoading

/** Lista de álbumes (HU01). */
class AlbumListFragment : Fragment(R.layout.fragment_album_list) {

    private val roleViewModel: RoleViewModel by activityViewModels { RoleViewModel.Factory }
    private val viewModel: AlbumListViewModel by viewModels { AlbumListViewModel.Factory }

    private var scrollToNewAlbum = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentAlbumListBinding.bind(view)
        setUpCatalog(binding)
        setUpCreateAlbumButton(binding)
        showCreatedAlbumMessage()
    }

    /** Cuadrícula de álbumes y los estados de carga, vacío y error con «Reintentar». */
    private fun setUpCatalog(binding: FragmentAlbumListBinding) {
        val adapter = AlbumAdapter()
        binding.albumGrid.layoutManager = GridLayoutManager(requireContext(), resources.getInteger(R.integer.album_grid_columns))
        binding.albumGrid.adapter = adapter

        viewLifecycleOwner.collectWhileVisible(viewModel.state) { state ->
            when (state) {
                UiState.Loading ->
                    binding.albumsLoading.showLoading(getString(R.string.album_list_loading), R.layout.view_skeleton_grid)
                UiState.Empty -> binding.albumsMessage.showEmpty(
                    getString(R.string.album_list_empty_title),
                    getString(R.string.album_list_empty_body),
                    R.drawable.ic_album,
                )
                UiState.Error -> binding.albumsMessage.showError(getString(R.string.album_list_object)) { viewModel.retry() }
                is UiState.Success -> adapter.submitList(state.data) {
                    // El álbum recién creado queda primero, así que se sube para que se vea
                    if (scrollToNewAlbum) binding.albumGrid.scrollToPosition(0)
                    scrollToNewAlbum = false
                }
            }

            binding.albumsLoading.root.isVisible = state == UiState.Loading
            binding.albumsMessage.root.isVisible = state == UiState.Empty || state == UiState.Error
            binding.albumGrid.isVisible = state is UiState.Success
        }
    }

    /** Botón «+»: solo para el coleccionista, abre Crear álbum y los avisos quedan encima. */
    private fun setUpCreateAlbumButton(binding: FragmentAlbumListBinding) {
        val button = binding.createAlbumButton

        viewLifecycleOwner.collectWhileVisible(roleViewModel.role) { role -> button.isVisible = role == Role.COLLECTOR }

        button.setOnClickListener { findNavController().navigateIfPossible(R.id.action_albumList_to_albumCreate) }

        keepSnackbarsAbove(button)
    }

    /** Al volver de Crear álbum con un álbum nuevo aparece el aviso «Álbum creado» y el álbum se marca como «Nuevo». */
    private fun showCreatedAlbumMessage() {
        val savedState = findNavController().currentBackStackEntry?.savedStateHandle ?: return

        viewLifecycleOwner.collectWhileVisible(savedState.getStateFlow<Int?>(AlbumCreateFragment.CREATED_ALBUM_ID, null)) { albumId ->
            if (albumId == null) return@collectWhileVisible
            showSnackbar(getString(R.string.snackbar_album_created))
            scrollToNewAlbum = true
            viewModel.onAlbumCreated(albumId)
            // Se quita para que el aviso no se repita al girar o volver a la pestaña
            savedState.remove<Int>(AlbumCreateFragment.CREATED_ALBUM_ID)
        }
    }
}
