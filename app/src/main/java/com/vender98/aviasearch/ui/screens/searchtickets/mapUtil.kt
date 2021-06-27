package com.vender98.aviasearch.ui.screens.searchtickets

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.vender98.aviasearch.R
import com.vender98.aviasearch.databinding.MarkerCityBinding
import com.vender98.aviasearch.extensions.rotate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.internal.toImmutableList
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.pow

//https://stackoverflow.com/a/35800880/11935726
@Suppress("FunctionName")
fun CityMarkerBitmap(context: Context, city: String): Bitmap {
    val markerView =
        MarkerCityBinding
            .inflate(LayoutInflater.from(context))
            .apply { root.text = city }
            .root
    markerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
    markerView.layout(0, 0, markerView.measuredWidth, markerView.measuredHeight)
    val bitmap = Bitmap.createBitmap(markerView.measuredWidth, markerView.measuredHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN)
    val drawable = markerView.background
    drawable.draw(canvas)
    markerView.draw(canvas)
    return bitmap
}

//https://stackoverflow.com/a/35574535/11935726
@Suppress("FunctionName")
fun PointMarkerBitmap(context: Context): Bitmap {
    val pointDrawable = AppCompatResources.getDrawable(context, R.drawable.marker_point)!!
    val canvas = Canvas()
    val bitmap =
        Bitmap.createBitmap(pointDrawable.intrinsicWidth, pointDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
    canvas.setBitmap(bitmap)
    pointDrawable.setBounds(0, 0, pointDrawable.intrinsicWidth, pointDrawable.intrinsicHeight)
    pointDrawable.draw(canvas)
    return bitmap
}

suspend fun generateBezierCurve(
    from: LatLng,
    to: LatLng,
    pointsCount: Int = 60
): List<LatLng> {
    val differentHorizontalHemisphere = abs(from.longitude - to.longitude) > 180
    val differentVerticalHemisphere = from.latitude * to.latitude < 0

    return if (differentVerticalHemisphere) { // Quadratic curve
        if (differentHorizontalHemisphere) {
            generateDifferentVerticalAndDifferentHorizontalHemisphereCurve(from, to, pointsCount)
        } else {
            generateDifferentVerticalAndSameHorizontalHemisphereCurve(from, to, pointsCount)
        }
    } else { // Cubic curve
        if (differentHorizontalHemisphere) {
            generateSameVerticalAndDifferentHorizontalHemisphereCurve(from, to, pointsCount)
        } else {
            generateSameVerticalAndHorizontalHemisphereCurve(from, to, pointsCount)
        }
    }
}

private suspend fun generateDifferentVerticalAndDifferentHorizontalHemisphereCurve(
    from: LatLng,
    to: LatLng,
    pointsCount: Int
): List<LatLng> {
    return generateQuadraticBezierCurve(
        p1 = LatLng(from.latitude, from.longitude - 180),
        p2 = LatLng(to.latitude, to.longitude - 180),
        pointsCount
    )
        .map { LatLng(it.latitude, it.longitude + 180) }
}

private suspend fun generateDifferentVerticalAndSameHorizontalHemisphereCurve(
    from: LatLng,
    to: LatLng,
    pointsCount: Int
): List<LatLng> = generateQuadraticBezierCurve(p1 = from, p2 = to, pointsCount)

private suspend fun generateSameVerticalAndDifferentHorizontalHemisphereCurve(
    from: LatLng,
    to: LatLng,
    pointsCount: Int
): List<LatLng> {
    val point1 =
        LatLng(from.latitude, from.longitude - 180)
    val point4 =
        LatLng(to.latitude, to.longitude - 180)
    val bounds =
        LatLngBounds.Builder()
            .include(point1)
            .include(point4)
            .build()
    val point2 = point1.rotate(relativePoint = bounds.center, angle = PI / 2)
    val point3 = point4.rotate(relativePoint = bounds.center, angle = PI / 2)
    return generateCubicBezierCurve(point1, point2, point3, point4, pointsCount)
        .map { LatLng(it.latitude, it.longitude + 180) }
}

private suspend fun generateSameVerticalAndHorizontalHemisphereCurve(
    from: LatLng,
    to: LatLng,
    pointsCount: Int
): List<LatLng> {
    val point1 =
        LatLng(from.latitude, from.longitude)
    val point4 =
        LatLng(to.latitude, to.longitude)
    val bounds =
        LatLngBounds.Builder()
            .include(point1)
            .include(point4)
            .build()
    val point2 = point1.rotate(relativePoint = bounds.center, angle = PI / 2)
    val point3 = point4.rotate(relativePoint = bounds.center, angle = PI / 2)
    return generateCubicBezierCurve(point1, point2, point3, point4, pointsCount)
}

suspend fun generateCubicBezierCurve(
    p1: LatLng,
    p2: LatLng,
    p3: LatLng,
    p4: LatLng,
    pointsCount: Int
): List<LatLng> = withContext(Dispatchers.Default) {
    val curvePoints = mutableListOf<LatLng>()

    val step = 1.0 / pointsCount
    var t = 0.0
    while (t < 1.0) {
        // P = (1−t)3P1 + 3(1−t)2tP2 +3(1−t)t2P3 + t3P4; for 4 points
        val arcX =
            ((1.0 - t).pow(3) * p1.latitude) +
                    (3 * (1.0 - t).pow(2) * t * p2.latitude) +
                    (3 * (1.0 - t) * t.pow(2) * p3.latitude) +
                    (t.pow(3) * p4.latitude)
        val arcY =
            ((1.0 - t).pow(3) * p1.longitude) +
                    (3 * (1.0 - t).pow(2) * t * p2.longitude) +
                    (3 * (1.0 - t) * t.pow(2) * p3.longitude) +
                    (t.pow(3) * p4.longitude)

        curvePoints.add(LatLng(arcX, arcY))
        t += step
    }

    curvePoints.toImmutableList()
}

suspend fun generateQuadraticBezierCurve(
    p1: LatLng,
    p2: LatLng,
    pointsCount: Int
): List<LatLng> = withContext(Dispatchers.Default) {
    val curvePoints = mutableListOf<LatLng>()

    val step = 1.0 / pointsCount
    var t = 0.0
    while (t < 1.0) {
        // P = (1-t)P1 +tP2; for 2 points
        val arcX =
            ((1.0 - t) * p1.latitude) +
                    (t * p2.latitude)
        val arcY =
            ((1.0 - t) * p1.longitude) +
                    (t * p2.longitude)

        curvePoints.add(LatLng(arcX, arcY))
        t += step
    }

    curvePoints.toImmutableList()
}