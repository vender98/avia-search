package com.vender98.aviasearch

import com.vender98.aviasearch.di.AppModule
import com.vender98.aviasearch.di.ScopeNames
import toothpick.Toothpick
import toothpick.configuration.Configuration

class Application : android.app.Application() {

    override fun onCreate() {
        super.onCreate()
        initToothpick()
    }

    private fun initToothpick() {
        Toothpick.setConfiguration(Configuration.forDevelopment())

        Toothpick
            .openScope(ScopeNames.APP_SCOPE)
            .installModules(AppModule())
    }

}