package com.vender98.aviasearch.ui.screens.searchtickets

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import com.vender98.aviasearch.databinding.MarkerCityBinding

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