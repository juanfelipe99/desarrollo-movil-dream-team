package com.equipo2.vinilos.ui.albums.create

import android.os.Bundle
import android.text.InputType
import android.view.View
import androidx.core.content.ContextCompat
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
import com.equipo2.vinilos.ui.common.showError
import com.equipo2.vinilos.ui.common.showPastDatePicker
import com.equipo2.vinilos.ui.common.toShortText
import com.google.android.material.progressindicator.CircularProgressIndicatorSpec
import com.google.android.material.progressindicator.IndeterminateDrawable
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import java.time.LocalDate

/** Formulario para crear un álbum (HU07). */
class AlbumCreateFragment : Fragment(R.layout.fragment_album_create) {

    private val viewModel: AlbumCreateViewModel by viewModels { AlbumCreateViewModel.Factory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentAlbumCreateBinding.bind(view)
        setUpTextFields(binding)
        setUpCoverPreview(binding)
        setUpReleaseDate(binding)
        setUpDropdowns(binding)
        setUpErrors(binding)
        setUpSaving(binding)

        // Cancelar cierra la pantalla igual que la X
        binding.cancelButton.setOnClickListener { findNavController().navigateUp() }
        binding.saveButton.setOnClickListener { save(binding) }
    }

    /** Lo que se escribe pasa al formulario del ViewModel. */
    private fun setUpTextFields(binding: FragmentAlbumCreateBinding) {
        binding.nameInput.doAfterTextChanged { text -> viewModel.update { copy(name = text.toString()) } }
        binding.coverInput.doAfterTextChanged { text -> viewModel.update { copy(cover = text.toString()) } }
        binding.descriptionInput.doAfterTextChanged { text -> viewModel.update { copy(description = text.toString()) } }
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
        val onPicked = { date: LocalDate -> viewModel.update { copy(releaseDate = date) } }
        val openPicker = {
            showPastDatePicker(DATE_PICKER_TAG, R.string.release_date_picker_title, viewModel.form.value.releaseDate, onPicked)
        }
        binding.releaseDateInput.setOnClickListener { openPicker() }
        binding.releaseDateField.setEndIconOnClickListener { openPicker() }
        reconnectDatePicker(DATE_PICKER_TAG, onPicked)

        val releaseDate = viewModel.form.map { it.releaseDate }.distinctUntilChanged()
        viewLifecycleOwner.collectWhileVisible(releaseDate) { date -> binding.releaseDateInput.setText(date?.toShortText()) }
    }

    /** Género y sello solo ofrecen los valores que acepta la API. */
    private fun setUpDropdowns(binding: FragmentAlbumCreateBinding) {
        binding.genreInput.bindOptions(AlbumCatalog.genres) { genre -> viewModel.update { copy(genre = genre) } }
        binding.recordLabelInput.bindOptions(AlbumCatalog.recordLabels) { label -> viewModel.update { copy(recordLabel = label) } }
    }

    private fun MaterialAutoCompleteTextView.bindOptions(options: List<String>, onSelected: (String) -> Unit) {
        setSimpleItems(options.toTypedArray())
        setOnItemClickListener { _, _, position, _ -> onSelected(options[position]) }
    }

    /** Cada campo muestra su error debajo, o nada si cumple su regla. */
    private fun setUpErrors(binding: FragmentAlbumCreateBinding) {
        viewLifecycleOwner.collectWhileVisible(viewModel.errors) { errors ->
            AlbumField.entries.forEach { field -> binding.fieldOf(field).showError(errors[field]) }
        }
    }

    /** Mientras guarda aparece la barra de progreso y Guardar queda deshabilitado con «Guardando…». */
    private fun setUpSaving(binding: FragmentAlbumCreateBinding) {
        val progress = buttonProgress()

        viewLifecycleOwner.collectWhileVisible(viewModel.isSaving) { saving ->
            binding.savingProgress.isVisible = saving
            binding.saveButton.isEnabled = !saving
            binding.saveButton.icon = progress.takeIf { saving }
            binding.saveButton.setText(if (saving) R.string.album_create_saving else R.string.action_save)
        }
    }

    /** Círculo de carga de 20 dp para el botón. */
    private fun buttonProgress(): IndeterminateDrawable<CircularProgressIndicatorSpec> {
        val context = requireContext()
        val spec = CircularProgressIndicatorSpec(context, null)
        spec.indicatorSize = resources.getDimensionPixelSize(R.dimen.button_progress)
        spec.trackThickness = resources.getDimensionPixelSize(R.dimen.button_progress_thickness)
        spec.indicatorInset = 0
        spec.indicatorColors = intArrayOf(ContextCompat.getColor(context, R.color.md_on_surface_variant))
        return IndeterminateDrawable.createCircularDrawable(context, spec)
    }

    /** Envía el álbum, y con errores lleva la pantalla al primero y si es un campo de texto le pasa el foco. */
    private fun save(binding: FragmentAlbumCreateBinding) {
        if (viewModel.save()) return

        val first = binding.fieldOf(viewModel.errors.value.keys.first())
        binding.formScroll.post { binding.formScroll.smoothScrollTo(0, first.top) }
        first.editText?.takeIf { it.inputType != InputType.TYPE_NULL }?.requestFocus()
    }

    private fun FragmentAlbumCreateBinding.fieldOf(field: AlbumField): TextInputLayout = when (field) {
        AlbumField.NAME -> nameField
        AlbumField.COVER -> coverField
        AlbumField.RELEASE_DATE -> releaseDateField
        AlbumField.GENRE -> genreField
        AlbumField.RECORD_LABEL -> recordLabelField
        AlbumField.DESCRIPTION -> descriptionField
    }

    private companion object {
        const val DATE_PICKER_TAG = "releaseDatePicker"
    }
}
