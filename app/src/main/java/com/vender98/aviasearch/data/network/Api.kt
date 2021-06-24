package com.vender98.aviasearch.data.network

import com.vender98.aviasearch.domain.CitySearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("/autocomplete")
    suspend fun searchCities(
        @Query(value = "term") term: String,
        @Query(value = "lang") lang: String
    ): CitySearchResponse
}