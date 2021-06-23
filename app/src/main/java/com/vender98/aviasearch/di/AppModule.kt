package com.vender98.aviasearch.di

import com.squareup.moshi.Moshi
import com.vender98.aviasearch.data.json.MoshiProvider
import com.vender98.aviasearch.data.network.Api
import com.vender98.aviasearch.data.network.ApiProvider
import com.vender98.aviasearch.data.network.OkHttpClientProvider
import com.vender98.aviasearch.presentation.common.ErrorBus
import com.vender98.aviasearch.presentation.common.ErrorHandler
import okhttp3.OkHttpClient
import toothpick.config.Module

class AppModule : Module() {

    init {
        bind(ErrorHandler::class.java).singleton()
        bind(ErrorBus::class.java).singleton()

        bind(Moshi::class.java)
            .toProvider(MoshiProvider::class.java)
            .providesSingleton()
        bind(OkHttpClient::class.java)
            .toProvider(OkHttpClientProvider::class.java)
            .providesSingleton()
        bind(Api::class.java)
            .toProvider(ApiProvider::class.java)
            .providesSingleton()
    }
}