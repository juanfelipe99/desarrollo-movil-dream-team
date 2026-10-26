package com.equipo2.vinilos.ui.artists.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import com.equipo2.vinilos.R
import com.equipo2.vinilos.data.model.Artist
import com.equipo2.vinilos.data.model.ArtistType
import com.equipo2.vinilos.databinding.ItemArtistBinding
import com.equipo2.vinilos.ui.common.initialsOf

/** Filas del listado. Al tocar una avisa con su artista. */
class ArtistAdapter(
    private val onArtistClick: (Artist) -> Unit,
) : ListAdapter<Artist, ArtistAdapter.ArtistViewHolder>(ArtistDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val holder = ArtistViewHolder(ItemArtistBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        holder.itemView.setOnClickListener {
            // Al animar un cambio de la lista la posición puede no existir todavía
            val position = holder.bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) onArtistClick(getItem(position))
        }
        return holder
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) = holder.bind(getItem(position))

    class ArtistViewHolder(private val binding: ItemArtistBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(artist: Artist) {
            val context = binding.root.context
            val isMusician = artist.type == ArtistType.MUSICIAN

            binding.artistInitials.text = initialsOf(artist.name)
            // Sin imagen se borra la anterior de la fila reciclada y quedan las iniciales
            binding.artistPhoto.load(artist.image.ifBlank { null })
            binding.artistName.text = artist.name
            binding.artistTypeIcon.setImageResource(if (isMusician) R.drawable.ic_mic else R.drawable.ic_groups)
            binding.artistTypeLabel.setText(if (isMusician) R.string.artist_type_musician else R.string.artist_type_band)
            binding.artistYear.text = context.getString(
                if (isMusician) R.string.artist_list_born_in else R.string.artist_list_created_in,
                artist.date.year,
            )
        }
    }

    private object ArtistDiff : DiffUtil.ItemCallback<Artist>() {
        override fun areItemsTheSame(oldItem: Artist, newItem: Artist) = oldItem.type == newItem.type && oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Artist, newItem: Artist) = oldItem == newItem
    }
}
