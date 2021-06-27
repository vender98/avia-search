package com.vender98.aviasearch.presentation.screens.searchtickets

import android.animation.ValueAnimator
import android.view.animation.LinearInterpolator
import androidx.core.animation.addListener
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.ktx.utils.sphericalHeading
import com.google.maps.android.ktx.utils.withSphericalLinearInterpolation
import com.vender98.aviasearch.ui.screens.searchtickets.PlanePoint
import com.vender98.aviasearch.ui.screens.searchtickets.Route
import com.vender98.aviasearch.ui.screens.searchtickets.generateBezierCurve
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.math.pow

class SearchTicketsViewModel @Inject constructor(
    private val route: Route
) : ViewModel() {

    private val _routeFlow = MutableStateFlow<Route?>(null)
    val routeFlow: Flow<Route> = _routeFlow.asStateFlow().filterNotNull()

    private val _bezierCurvePointsFlow = MutableStateFlow<List<LatLng>?>(null)
    val bezierCurvePointsFlow: Flow<List<LatLng>> = _bezierCurvePointsFlow.asStateFlow().filterNotNull()

    private val _planePointFlow = MutableStateFlow<PlanePoint?>(null)
    val planePointFlow: Flow<PlanePoint> = _planePointFlow.asStateFlow().filterNotNull()

    init {
        _routeFlow.tryEmit(route)
        generateBezierCurvePoints()
        animatePlane()
    }

    private fun generateBezierCurvePoints() {
        viewModelScope.launch {
            val curve = generateBezierCurve(from = route.departureCity.location, to = route.destinationCity.location)
            _bezierCurvePointsFlow.tryEmit(curve)
        }
    }

    private fun animatePlane() {
        viewModelScope.launch {
            val curvePoints = bezierCurvePointsFlow.first()
            var index = 0
            while (index < curvePoints.lastIndex) {
                val currentPoint = curvePoints[index]
                val nextPoint = curvePoints[index + 1]
                animatePlane(from = currentPoint, to = nextPoint, durationFactor = index.toDouble() / curvePoints.size)
                index++
            }
        }
    }

    private suspend fun animatePlane(from: LatLng, to: LatLng, durationFactor: Double) =
        suspendCancellableCoroutine<Unit> { continuation ->
            ValueAnimator.ofFloat(0.0f, 1.0f).apply {
                addUpdateListener { animation ->
                    _planePointFlow.value = PlanePoint(
                        location = from.withSphericalLinearInterpolation(
                            to,
                            fraction = (animation.animatedValue as Float).toDouble()
                        ),
                        angle = from.sphericalHeading(to)
                    )
                }
                addListener(onEnd = { continuation.resume(Unit) })
                interpolator = LinearInterpolator()
                duration = 50 * ((1.0 / (durationFactor - 1.0).pow(4))).toLong()
                start()
            }
        }
}