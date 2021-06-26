package com.vender98.aviasearch.extensions

import com.google.android.gms.maps.model.LatLng
import kotlin.math.cos
import kotlin.math.sin

fun LatLng.rotate(relativePoint: LatLng, angle: Double) = LatLng(
    relativePoint.latitude +
            (this.latitude - relativePoint.latitude) * cos(angle) -
            (this.longitude - relativePoint.longitude) * sin(angle),
    relativePoint.longitude +
            (this.latitude - relativePoint.latitude) * sin(angle) +
            (this.longitude - relativePoint.longitude) * cos(angle)
)