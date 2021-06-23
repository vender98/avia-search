package com.vender98.aviasearch.data.network

import retrofit2.http.GET

interface Api {

    @GET("/autocomplete")
    suspend fun search()
}