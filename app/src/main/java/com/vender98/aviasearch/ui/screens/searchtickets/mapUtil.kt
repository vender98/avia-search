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
import com.vender98.aviasearch.R
import com.vender98.aviasearch.databinding.MarkerCityBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.internal.toImmutableList
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

suspend fun generateCubicBezierCurve(
    p1: LatLng,
    p2: LatLng,
    p3: LatLng,
    p4: LatLng,
    count: Int = 60
): List<LatLng> = withContext(Dispatchers.Default) {
    val curvePoints = mutableListOf<LatLng>()

    val step = 1.0 / count
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