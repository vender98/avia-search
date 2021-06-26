package com.vender98.aviasearch.presentation.screens.searchtickets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.vender98.aviasearch.extensions.rotate
import com.vender98.aviasearch.ui.screens.searchtickets.Route
import com.vender98.aviasearch.ui.screens.searchtickets.generateCubicBezierCurve
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.PI

class SearchTicketsViewModel @Inject constructor(
    private val route: Route
) : ViewModel() {

    private val _routeFlow = MutableStateFlow<Route?>(null)
    val routeFlow: Flow<Route> = _routeFlow.asStateFlow().filterNotNull()

    private val _bezierCurvePointsFlow = MutableStateFlow<List<LatLng>?>(null)
    val bezierCurvePointsFlow: Flow<List<LatLng>> = _bezierCurvePointsFlow.asStateFlow().filterNotNull()

    init {
        _routeFlow.tryEmit(route)
        generateBezierCurvePoints()
    }

    private fun generateBezierCurvePoints() {
        viewModelScope.launch {
            val routeBounds =
                LatLngBounds.Builder()
                    .include(route.departureCity.location)
                    .include(route.destinationCity.location)
                    .build()

            val point1 = route.departureCity.location
            val point4 = route.destinationCity.location
            val point2 = point1.rotate(relativePoint = routeBounds.center, angle = PI / 2)
            val point3 = point4.rotate(relativePoint = routeBounds.center, angle = PI / 2)

            val curve = generateCubicBezierCurve(point1, point2, point3, point4)
            _bezierCurvePointsFlow.tryEmit(curve)
        }
    }
}