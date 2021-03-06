package com.vender98.aviasearch.di

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import toothpick.Toothpick
import toothpick.config.Module

class ToothpickViewModelFactory(
    private val bindDependencies: Module.() -> Unit = {}
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        Toothpick
            .openScopes(ScopeNames.APP_SCOPE, this.javaClass.simpleName)
            .installModules(Module().apply {
                bindDependencies()
            })
            .getInstance(modelClass)
            .also {
                Toothpick.closeScope(this.javaClass.simpleName)
            }
}

class ToothpickAndroidViewModelFactory(private val application: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        Toothpick
            .openScopes(ScopeNames.APP_SCOPE, this.javaClass.simpleName)
            .installModules(Module().apply {
                bind(Application::class.java).toInstance(application)
            })
            .getInstance(modelClass)
            .also {
                Toothpick.closeScope(this.javaClass.simpleName)
            }

}