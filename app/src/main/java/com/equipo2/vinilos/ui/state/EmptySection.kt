package com.equipo2.vinilos.ui.state

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import com.equipo2.vinilos.databinding.ViewEmptySectionBinding

/** Agrega la tarjeta punteada de sección vacía, por ejemplo cuando un álbum no tiene tracks. */
fun ViewGroup.addEmptySection(@DrawableRes icon: Int, title: CharSequence, body: CharSequence) {
    val binding = ViewEmptySectionBinding.inflate(LayoutInflater.from(context), this, true)
    binding.emptySectionIcon.setImageResource(icon)
    binding.emptySectionTitle.text = title
    binding.emptySectionBody.text = body
}
