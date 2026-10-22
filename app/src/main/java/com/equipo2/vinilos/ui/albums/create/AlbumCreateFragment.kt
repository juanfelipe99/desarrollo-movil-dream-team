package com.equipo2.vinilos.ui.albums.create

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil3.load
import com.equipo2.vinilos.R
import com.equipo2.vinilos.data.model.AlbumCatalog
import com.equipo2.vinilos.databinding.FragmentAlbumCreateBinding
import com.equipo2.vinilos.ui.common.collectWhileVisible
import com.equipo2.vinilos.ui.common.reconnectDatePicker
import com.equipo2.vinilos.ui.common.showPastDatePicker
import com.equipo2.vinilos.ui.common.toShortText

/** Formulario para crear un álbum (HU07). */
class AlbumCreateFragment : Fragment(R.layout.fragment_album_create) {

    private val viewModel: AlbumCreateViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentAlbumCreateBinding.bind(view)
        setUpCoverPreview(binding)
        setUpReleaseDate(binding)
        setUpDropdowns(binding)

        // Cancelar cierra la pantalla igual que la X
        binding.cancelButton.setOnClickListener { findNavController().navigateUp() }
    }

    /** Con una URL válida aparece debajo la portada, o el marcador si no se puede cargar. */
    private fun setUpCoverPreview(binding: FragmentAlbumCreateBinding) {
        binding.coverInput.doAfterTextChanged { text ->
            val url = text.toString().trim()
            val isValid = isValidCoverUrl(url)

            binding.coverPreview.isVisible = isValid
            if (isValid && url != binding.coverPreviewUrl.text.toString()) {
                binding.coverPreviewUrl.text = url
                binding.coverPreviewImage.load(url)
            }
        }
    }

    /** El campo de fecha y su ícono abren el selector, y el campo muestra la fecha elegida. */
    private fun setUpReleaseDate(binding: FragmentAlbumCreateBinding) {
        val openPicker = {
            showPastDatePicker(DATE_PICKER_TAG, R.string.release_date_picker_title, viewModel.releaseDate.value, viewModel::setReleaseDate)
        }
        binding.releaseDateInput.setOnClickListener { openPicker() }
        binding.releaseDateField.setEndIconOnClickListener { openPicker() }
        reconnectDatePicker(DATE_PICKER_TAG, viewModel::setReleaseDate)

        viewLifecycleOwner.collectWhileVisible(viewModel.releaseDate) { date ->
            binding.releaseDateInput.setText(date?.toShortText())
        }
    }

    /** Género y sello solo ofrecen los valores que acepta la API. */
    private fun setUpDropdowns(binding: FragmentAlbumCreateBinding) {
        binding.genreInput.setSimpleItems(AlbumCatalog.genres.toTypedArray())
        binding.recordLabelInput.setSimpleItems(AlbumCatalog.recordLabels.toTypedArray())
    }

    private companion object {
        const val DATE_PICKER_TAG = "releaseDatePicker"
    }
}
