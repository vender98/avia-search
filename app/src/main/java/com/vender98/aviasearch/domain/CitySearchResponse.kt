package com.vender98.aviasearch.domain

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CitySearchResponse(
    @Json(name = "cities") val cities: List<City>
)