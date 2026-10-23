
package com.equipo2.vinilos.ui.albums.detail

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.equipo2.vinilos.R
import com.equipo2.vinilos.data.model.Track
import com.equipo2.vinilos.data.model.AlbumComment
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.launch

/**
 * Pantalla de detalle de álbum (HU02).
 */
class AlbumDetailFragment : Fragment(R.layout.fragment_album_detail) {

    private val viewModel: AlbumDetailViewModel by viewModels {
        val albumId = arguments?.getInt(ARG_ALBUM_ID) ?: 4
        AlbumDetailViewModel.factory(albumId)
    }

    private var tracksExpanded = false
    private var commentsExpanded = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val statusContainer =
            view.findViewById<LinearLayout>(R.id.albumStatusContainer)
        val loadingContent =
            view.findViewById<LinearLayout>(R.id.albumLoadingContent)
        val errorContent =
            view.findViewById<LinearLayout>(R.id.albumErrorContent)
        val albumContent =
            view.findViewById<LinearLayout>(R.id.albumContentContainer)

        val retryButton =
            view.findViewById<View>(R.id.retryAlbumButton)

        retryButton.setOnClickListener {
            viewModel.loadAlbum()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.isLoading.collect { isLoading ->
                        if (isLoading) {
                            statusContainer.visibility = View.VISIBLE
                            loadingContent.visibility = View.VISIBLE
                            errorContent.visibility = View.GONE
                            albumContent.visibility = View.GONE
                        } else if (viewModel.error.value == null &&
                            viewModel.album.value != null
                        ) {
                            statusContainer.visibility = View.GONE
                            albumContent.visibility = View.VISIBLE
                        }
                    }
                }

                launch {
                    viewModel.error.collect { error ->
                        if (error != null) {
                            statusContainer.visibility = View.VISIBLE
                            loadingContent.visibility = View.GONE
                            errorContent.visibility = View.VISIBLE
                            albumContent.visibility = View.GONE
                        }
                    }
                }
            }
        }

        val cover =
            view.findViewById<android.widget.ImageView>(R.id.albumCover)
        cover.setBackgroundColor(android.graphics.Color.rgb(230, 223, 240))
        cover.setPadding(dp(28), dp(28), dp(28), dp(28))
        cover.scaleType = android.widget.ImageView.ScaleType.FIT_CENTER
        cover.setColorFilter(android.graphics.Color.rgb(125, 109, 148))

        viewModel.loadAlbum()

        val description =
            view.findViewById<TextView>(R.id.albumDescription)
        val expandDescription =
            view.findViewById<TextView>(R.id.expandDescription)

        expandDescription.setOnClickListener {
            val expanded = description.maxLines == 4
            description.maxLines = if (expanded) Int.MAX_VALUE else 4
            expandDescription.setText(
                if (expanded) R.string.album_detail_collapse
                else R.string.album_detail_expand
            )
        }

        android.util.Log.d(
            "AlbumDetail",
            "Consultando álbum desde el Fragment"
        )

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.album.collect { album ->
                    album?.let { albumData ->
                        view.findViewById<TextView>(R.id.albumName).text =
                            albumData.name

                        view.findViewById<TextView>(R.id.albumReleaseDate).text =
                            albumData.releaseDate.toString()

                        view.findViewById<TextView>(R.id.albumGenre).text =
                            albumData.genre

                        view.findViewById<TextView>(R.id.albumRecordLabel).text =
                            albumData.recordLabel

                        view.findViewById<TextView>(R.id.albumDescription).text =
                            albumData.description

                        view.findViewById<TextView>(R.id.tracksCount).text =
                            albumData.tracks.size.toString()

                        view.findViewById<TextView>(R.id.commentsCount).text =
                            albumData.comments.size.toString()

                        renderTracks(
                            view.findViewById(R.id.tracksContainer),
                            albumData.tracks
                        )

                        renderComments(
                            view.findViewById(R.id.commentsContainer),
                            albumData.comments
                        )
                    }
                }
            }
        }
    }

    private fun renderTracks(
        container: LinearLayout,
        tracks: List<Track>
    ) {
        container.removeAllViews()

        if (tracks.isEmpty()) {
            val card = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER_VERTICAL
                setPadding(dp(12), dp(10), dp(12), dp(10))
                background = android.graphics.drawable.GradientDrawable().apply {
                    shape = android.graphics.drawable.GradientDrawable.RECTANGLE
                    cornerRadius = dp(8).toFloat()
                    setColor(android.graphics.Color.TRANSPARENT)
                    setStroke(
                        dp(1),
                        android.graphics.Color.rgb(165, 145, 190),
                        dp(4).toFloat(),
                        dp(3).toFloat()
                    )
                }
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }

            val icon = TextView(requireContext()).apply {
                text = "♫"
                textSize = 20f
                gravity = Gravity.CENTER
                setTextColor(android.graphics.Color.rgb(92, 78, 115))
                layoutParams = LinearLayout.LayoutParams(
                    dp(28),
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }

            val textContainer = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(dp(8), 0, 0, 0)
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1f
                )
            }

            val title = TextView(requireContext()).apply {
                text = "Este álbum aún no tiene tracks"
                setTextAppearance(
                    requireContext(),
                    com.google.android.material.R.style
                        .TextAppearance_Material3_BodySmall
                )
                setTextColor(android.graphics.Color.rgb(35, 31, 40))
            }

            val message = TextView(requireContext()).apply {
                text = "Las canciones aparecerán aquí cuando se registren."
                setPadding(0, dp(2), 0, 0)
                setTextAppearance(
                    requireContext(),
                    com.google.android.material.R.style
                        .TextAppearance_Material3_BodySmall
                )
                setTextColor(android.graphics.Color.rgb(100, 94, 108))
            }

            textContainer.addView(title)
            textContainer.addView(message)
            card.addView(icon)
            card.addView(textContainer)
            container.addView(card)
            return
        }

        val visibleTracks =
            if (tracksExpanded) tracks else tracks.take(3)

        visibleTracks.forEachIndexed { index, track ->
            val row = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER_VERTICAL
                setPadding(dp(10), 0, dp(10), 0)
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    dp(40)
                )
            }

            val number = TextView(requireContext()).apply {
                text = (index + 1).toString()
                setTextAppearance(
                    requireContext(),
                    com.google.android.material.R.style
                        .TextAppearance_Material3_BodySmall
                )
            }

            val name = TextView(requireContext()).apply {
                text = track.name
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1f
                )
                setPadding(dp(10), 0, dp(8), 0)
                setTextAppearance(
                    requireContext(),
                    com.google.android.material.R.style
                        .TextAppearance_Material3_BodyMedium
                )
            }

            val duration = TextView(requireContext()).apply {
                text = track.duration
                setTextAppearance(
                    requireContext(),
                    com.google.android.material.R.style
                        .TextAppearance_Material3_BodySmall
                )
            }

            row.addView(
                number,
                LinearLayout.LayoutParams(
                    dp(20),
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            )
            row.addView(name)
            row.addView(duration)
            container.addView(row)

            if (index < visibleTracks.lastIndex) {
                val divider = View(requireContext()).apply {
                    setBackgroundColor(0xFFE0E0E0.toInt())
                }

                container.addView(
                    divider,
                    LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(1)
                    )
                )
            }
        }

        if (tracks.size > 3) {
            container.addView(
                createListToggle(
                    expanded = tracksExpanded,
                    expandText = "Ver todas",
                    collapseText = "Ver menos"
                ) {
                    tracksExpanded = !tracksExpanded
                    renderTracks(container, tracks)
                }
            )
        }
    }

    private fun renderComments(
        container: LinearLayout,
        comments: List<AlbumComment>
    ) {
        container.removeAllViews()

        if (comments.isEmpty()) {
            val card = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER_VERTICAL
                setPadding(dp(12), dp(10), dp(12), dp(10))
                background = android.graphics.drawable.GradientDrawable().apply {
                    shape = android.graphics.drawable.GradientDrawable.RECTANGLE
                    cornerRadius = dp(8).toFloat()
                    setColor(android.graphics.Color.TRANSPARENT)
                    setStroke(
                        dp(1),
                        android.graphics.Color.rgb(165, 145, 190),
                        dp(4).toFloat(),
                        dp(3).toFloat()
                    )
                }
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }

            val icon = TextView(requireContext()).apply {
                text = "♧"
                textSize = 20f
                gravity = Gravity.CENTER
                setTextColor(android.graphics.Color.rgb(92, 78, 115))
                layoutParams = LinearLayout.LayoutParams(
                    dp(28),
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }

            val textContainer = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(dp(8), 0, 0, 0)
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1f
                )
            }

            val title = TextView(requireContext()).apply {
                text = "Sin comentarios"
                setTextAppearance(
                    requireContext(),
                    com.google.android.material.R.style
                        .TextAppearance_Material3_BodySmall
                )
                setTextColor(android.graphics.Color.rgb(35, 31, 40))
            }

            val message = TextView(requireContext()).apply {
                text = "Ningún coleccionista ha comentado este álbum todavía."
                setPadding(0, dp(2), 0, 0)
                setTextAppearance(
                    requireContext(),
                    com.google.android.material.R.style
                        .TextAppearance_Material3_BodySmall
                )
                setTextColor(android.graphics.Color.rgb(100, 94, 108))
            }

            textContainer.addView(title)
            textContainer.addView(message)
            card.addView(icon)
            card.addView(textContainer)
            container.addView(card)
            return
        }

        val visibleComments =
            if (commentsExpanded) comments else comments.take(3)

        visibleComments.forEach { comment ->
            val card = MaterialCardView(requireContext()).apply {
                radius = dp(12).toFloat()
                cardElevation = 0f
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    bottomMargin = dp(8)
                }
            }

            val content = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(dp(12), dp(12), dp(12), dp(12))
            }

            val rating = TextView(requireContext()).apply {
                val stars = "★".repeat(comment.rating.coerceIn(0, 5))
                text = "$stars  ${comment.rating}/5"
                setTextColor(android.graphics.Color.rgb(211, 84, 0))
            }

            val description = TextView(requireContext()).apply {
                text = comment.description
                setPadding(0, dp(8), 0, 0)
            }

            content.addView(rating)
            content.addView(description)
            card.addView(content)
            container.addView(card)
        }

        if (comments.size > 3) {
            container.addView(
                createListToggle(
                    expanded = commentsExpanded,
                    expandText = "Ver todos",
                    collapseText = "Ver menos"
                ) {
                    commentsExpanded = !commentsExpanded
                    renderComments(container, comments)
                }
            )
        }
    }

    private fun createListToggle(
        expanded: Boolean,
        expandText: String,
        collapseText: String,
        onClick: () -> Unit
    ): TextView {
        return TextView(requireContext()).apply {
            text = if (expanded) collapseText else expandText
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                dp(48)
            )
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dp(10), 0, dp(10), 0)
            setTextAppearance(
                requireContext(),
                com.google.android.material.R.style
                    .TextAppearance_Material3_BodyMedium
            )
            setTextColor(android.graphics.Color.rgb(125, 109, 148))
            setOnClickListener { onClick() }
        }
    }

    private fun dp(value: Int): Int =
        (value * resources.displayMetrics.density).toInt()

    companion object {
        private const val ARG_ALBUM_ID = "albumId"
    }
}
