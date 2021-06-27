package com.vender98.aviasearch.ui.screens.searchtickets

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.google.maps.android.ktx.awaitMap
import com.vender98.aviasearch.R
import com.vender98.aviasearch.databinding.FragmentSearchTicketsBinding
import com.vender98.aviasearch.di.ToothpickViewModelFactory
import com.vender98.aviasearch.presentation.screens.searchtickets.SearchTicketsViewModel
import com.vender98.aviasearch.ui.base.BaseFragment
import com.vender98.aviasearch.util.CityMarkerBitmap
import com.vender98.aviasearch.util.PointMarkerBitmap
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SearchTicketsFragment : BaseFragment(R.layout.fragment_search_tickets) {

    private val binding: FragmentSearchTicketsBinding by viewBinding()
    private val viewModel: SearchTicketsViewModel by viewModels {
        ToothpickViewModelFactory(bindDependencies = {
            bind(Route::class.java).toInstance(requireArguments()[ROUTE_ARG] as Route)
        })
    }

    private var map: GoogleMap? = null
    private var planeMarker: Marker? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding.mapView) {
            onCreate(savedInstanceState?.getBundle(MAPVIEW_BUNDLE_KEY))
            lifecycleScope.launch {
                val map = awaitMap()
                this@SearchTicketsFragment.map = map
                map.setOnMarkerClickListener { true }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY) ?: Bundle().also {
            outState.putBundle(MAPVIEW_BUNDLE_KEY, it)
        }
        binding.mapView.onSaveInstanceState(mapViewBundle)
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
        observeMapData()
    }

    private fun observeMapData() {
        lifecycleScope.launchWhenStartedUntilStop {
            // To start observing map-specific data after the map is ready
            binding.mapView.awaitMap()

            lifecycleScope.launchWhenStartedUntilStop {
                viewModel.routeFlow.collect { showRouteEndpoints(it) }
            }
            lifecycleScope.launchWhenStartedUntilStop {
                viewModel.bezierCurvePointsFlow.collect { showCurvePoints(it) }
            }
            lifecycleScope.launchWhenStartedUntilStop {
                viewModel.planePointFlow.collect { showPlane(it) }
            }
        }
    }

    private fun showRouteEndpoints(route: Route) {
        val map = this.map ?: return

        val departureMarkerOptions =
            MarkerOptions()
                .position(route.departureCity.location)
                .icon(
                    BitmapDescriptorFactory.fromBitmap(
                        CityMarkerBitmap(requireContext(), route.departureCity.name)
                    )
                )
                .zIndex(CITY_MARKER_Z_INDEX)
        val destinationMarkerOptions =
            MarkerOptions()
                .position(route.destinationCity.location)
                .icon(
                    BitmapDescriptorFactory.fromBitmap(
                        CityMarkerBitmap(requireContext(), route.destinationCity.name)
                    )
                )
                .zIndex(CITY_MARKER_Z_INDEX)
        map.addMarker(departureMarkerOptions)
        map.addMarker(destinationMarkerOptions)

        val bounds = LatLngBounds.Builder()
            .include(route.departureCity.location)
            .include(route.destinationCity.location)
            .build()
        val padding =
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                (resources.displayMetrics.widthPixels * 0.2).toInt()
            } else {
                (resources.displayMetrics.heightPixels * 0.2).toInt()
            }

        val boundsUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)
        map.moveCamera(boundsUpdate)
    }

    private fun showCurvePoints(points: List<LatLng>) {
        val map = map ?: return

        val markerIcon = BitmapDescriptorFactory.fromBitmap(PointMarkerBitmap(requireContext()))
        for (point in points) {
            val markerOptions =
                MarkerOptions()
                    .position(point)
                    .icon(markerIcon)
                    .anchor(0.5f, 0.5f)
                    .zIndex(CURVE_MARKER_Z_INDEX)
            map.addMarker(markerOptions)
        }
    }

    private fun showPlane(point: PlanePoint) {
        val planeMarker = planeMarker ?: initPlane(point.location, map ?: return)

        planeMarker.position = point.location
        planeMarker.rotation = point.angle.toFloat() - 90f
    }

    private fun initPlane(location: LatLng, map: GoogleMap): Marker {
        val planeMarkerOptions =
            MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_plane))
                .position(location)
                .anchor(0.5f, 0.5f)
                .zIndex(PLANE_MARKER_Z_INDEX)
        planeMarker = map.addMarker(planeMarkerOptions)
        return planeMarker!!
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onDestroyView() {
        map = null
        binding.mapView.onDestroy()
        super.onDestroyView()
    }

    companion object {
        private const val ROUTE_ARG = "ROUTE_ARG"
        private const val MAPVIEW_BUNDLE_KEY = "MAPVIEW_BUNDLE_KEY"

        private const val CURVE_MARKER_Z_INDEX = 1.0F
        private const val CITY_MARKER_Z_INDEX = 2.0F
        private const val PLANE_MARKER_Z_INDEX = 3.0F

        fun newInstance(route: Route) = SearchTicketsFragment().apply {
            arguments = bundleOf(ROUTE_ARG to route)
        }
    }
}