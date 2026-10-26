package com.equipo2.vinilos.ui.collectors

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.equipo2.vinilos.R
import com.equipo2.vinilos.databinding.FragmentCollectorListBinding
import com.equipo2.vinilos.ui.state.showEmpty

/** Lista de coleccionistas (HU05). Hasta el sprint 2 solo avisa que llegará en la próxima versión. */
class CollectorListFragment : Fragment(R.layout.fragment_collector_list) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        FragmentCollectorListBinding.bind(view).collectorsSoon.showEmpty(
            getString(R.string.coming_soon_title),
            getString(R.string.collector_list_soon_body),
            R.drawable.ic_groups,
        )
    }
}
