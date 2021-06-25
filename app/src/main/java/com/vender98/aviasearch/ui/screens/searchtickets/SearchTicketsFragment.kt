package com.vender98.aviasearch.ui.screens.searchtickets

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.vender98.aviasearch.R
import com.vender98.aviasearch.databinding.FragmentSearchTicketsBinding
import com.vender98.aviasearch.ui.base.BaseFragment

class SearchTicketsFragment : BaseFragment(R.layout.fragment_search_tickets), OnMapReadyCallback {

    private val binding: FragmentSearchTicketsBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            mapView.onCreate(savedInstanceState)
            mapView.getMapAsync(this@SearchTicketsFragment)
        }
    }

    override fun onMapReady(map: GoogleMap) {
        // TODO: work with map
    }

    companion object {
        private const val ROUTE_ARG = "ROUTE_ARG"

        fun newInstance(route: Route) = SearchTicketsFragment().apply {
            arguments = bundleOf(ROUTE_ARG to route)
        }
    }
}