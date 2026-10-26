package com.equipo2.vinilos.ui.albums.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.equipo2.vinilos.R
import com.equipo2.vinilos.databinding.ItemAlbumCountBinding

/** Encabezado de la cuadrícula con el número de álbumes, que se desplaza con las tarjetas. */
class AlbumCountAdapter : RecyclerView.Adapter<AlbumCountAdapter.CountViewHolder>() {

    var count: Int? = null
        set(value) {
            if (field == value) return
            val hadHeader = field != null
            field = value
            when {
                value == null -> notifyItemRemoved(0)
                hadHeader -> notifyItemChanged(0)
                else -> notifyItemInserted(0)
            }
        }

    override fun getItemCount() = if (count == null) 0 else 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CountViewHolder(ItemAlbumCountBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: CountViewHolder, position: Int) {
        val count = count ?: return
        holder.binding.albumCount.text = holder.itemView.resources.getQuantityString(R.plurals.album_list_count, count, count)
    }

    class CountViewHolder(val binding: ItemAlbumCountBinding) : RecyclerView.ViewHolder(binding.root)
}
