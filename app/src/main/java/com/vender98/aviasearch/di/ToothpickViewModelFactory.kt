package com.vender98.aviasearch.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import toothpick.Toothpick

class ToothpickViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        Toothpick
            .openScope(ScopeNames.APP_SCOPE)
            .getInstance(modelClass)
}