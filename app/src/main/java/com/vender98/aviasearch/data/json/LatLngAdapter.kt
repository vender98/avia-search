package com.vender98.aviasearch.data.json

import com.google.android.gms.maps.model.LatLng
import com.squareup.moshi.FromJson
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.ToJson

object LatLngAdapter {

    @FromJson
    fun fromJson(json: LatLngJson) = LatLng(json.lat, json.lon)

    @ToJson
    fun toJson(latLng: LatLng) = LatLngJson(latLng.latitude, latLng.longitude)

    @JsonClass(generateAdapter = true)
    data class LatLngJson(
        @Json(name = "lat") val lat: Double,
        @Json(name = "lon") val lon: Double
    )
}