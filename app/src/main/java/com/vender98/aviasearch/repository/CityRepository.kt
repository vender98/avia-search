package com.vender98.aviasearch.repository

import com.vender98.aviasearch.data.network.Api
import com.vender98.aviasearch.domain.City
import javax.inject.Inject

class CityRepository @Inject constructor(
    private val api: Api
) {

    suspend fun searchCities(input: String): List<City> =
        api
            .searchCities(term = input, lang = SEARCH_LANGUAGE)
            .cities

    companion object {
        private const val SEARCH_LANGUAGE = "ru"
    }
}