package com.vender98.aviasearch.domain

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class City(
    @Json(name = "city") val name: String,
    @Json(name = "location") val location: LatLng
) : Parcelable