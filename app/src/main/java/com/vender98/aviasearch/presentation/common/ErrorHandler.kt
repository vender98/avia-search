package com.vender98.aviasearch.presentation.common

import android.util.Log
import java.util.concurrent.CancellationException
import javax.inject.Inject

class ErrorHandler @Inject constructor(
    private val errorBus: ErrorBus
) {

    fun proceed(error: Throwable) {
        Log.e(null, error.message, error)
        if (error !is CancellationException) {
            errorBus.put("${error.javaClass.simpleName}: ${error.message ?: error.toString()}")
        }
    }
}