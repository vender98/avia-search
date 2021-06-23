package com.vender98.aviasearch.di

import com.vender98.aviasearch.presentation.common.ErrorBus
import com.vender98.aviasearch.presentation.common.ErrorHandler
import toothpick.config.Module

class AppModule : Module() {

    init {
        bind(ErrorHandler::class.java).singleton()
        bind(ErrorBus::class.java).singleton()
    }
}