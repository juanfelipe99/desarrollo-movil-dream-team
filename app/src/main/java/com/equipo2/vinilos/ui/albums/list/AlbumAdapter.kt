package com.equipo2.vinilos.ui.albums.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import com.equipo2.vinilos.R
import com.equipo2.vinilos.data.model.Album
import com.equipo2.vinilos.databinding.ItemAlbumBinding

/** Tarjetas del catálogo: portada, nombre, año y género, y el distintivo «Nuevo». Al tocar una avisa con su álbum. */
class AlbumAdapter(
    private val onAlbumClick: (Album) -> Unit,
) : ListAdapter<AlbumItem, AlbumAdapter.AlbumViewHolder>(AlbumDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val holder = AlbumViewHolder(ItemAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        holder.itemView.setOnClickListener {
            // Al animar un cambio de la lista la posición puede no existir todavía
            val position = holder.bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) onAlbumClick(getItem(position).album)
        }
        return holder
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) = holder.bind(getItem(position))

    class AlbumViewHolder(private val binding: ItemAlbumBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AlbumItem) {
            val album = item.album
            val context = binding.root.context

            binding.albumCover.load(album.cover)
            binding.albumName.text = album.name
            binding.albumSubtitle.text = context.getString(R.string.album_list_subtitle, album.releaseDate.year, album.genre)
            binding.albumNewBadge.isVisible = item.isNew
        }
    }

    private object AlbumDiff : DiffUtil.ItemCallback<AlbumItem>() {
        override fun areItemsTheSame(oldItem: AlbumItem, newItem: AlbumItem) = oldItem.album.id == newItem.album.id
        override fun areContentsTheSame(oldItem: AlbumItem, newItem: AlbumItem) = oldItem == newItem
    }
}
