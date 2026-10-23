
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
import com.equipo2.vinilos.databinding.ViewLoadingBinding
import com.equipo2.vinilos.databinding.ViewStateMessageBinding
import com.equipo2.vinilos.ui.state.addEmptySection
import com.equipo2.vinilos.ui.state.showError
import com.equipo2.vinilos.ui.state.showLoading
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

        val loading = ViewLoadingBinding.bind(view.findViewById(R.id.albumLoading))
        val error = ViewStateMessageBinding.bind(view.findViewById(R.id.albumError))
        val albumContent =
            view.findViewById<LinearLayout>(R.id.albumContentContainer)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.isLoading.collect { isLoading ->
                        if (isLoading) {
                            loading.showLoading(getString(R.string.album_detail_loading), R.layout.view_skeleton_detail)
                            loading.root.visibility = View.VISIBLE
                            error.root.visibility = View.GONE
                            albumContent.visibility = View.GONE
                        } else if (viewModel.error.value == null &&
                            viewModel.album.value != null
                        ) {
                            loading.root.visibility = View.GONE
                            albumContent.visibility = View.VISIBLE
                        }
                    }
                }

                launch {
                    viewModel.error.collect { message ->
                        if (message != null) {
                            error.showError(getString(R.string.album_detail_object)) { viewModel.loadAlbum() }
                            loading.root.visibility = View.GONE
                            error.root.visibility = View.VISIBLE
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
                            resources.getQuantityString(
                                R.plurals.album_detail_tracks_count,
                                albumData.tracks.size,
                                albumData.tracks.size,
                            )

                        view.findViewById<TextView>(R.id.commentsCount).text =
                            resources.getQuantityString(
                                R.plurals.album_detail_comments_count,
                                albumData.comments.size,
                                albumData.comments.size,
                            )

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
            container.addEmptySection(
                R.drawable.ic_music_note,
                getString(R.string.album_detail_tracks_empty_title),
                getString(R.string.album_detail_tracks_empty_body),
            )
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
            container.addEmptySection(
                R.drawable.ic_chat_bubble,
                getString(R.string.album_detail_comments_empty_title),
                getString(R.string.album_detail_comments_empty_body),
            )
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
