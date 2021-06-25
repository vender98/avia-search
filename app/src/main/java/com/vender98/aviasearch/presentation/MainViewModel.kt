package com.vender98.aviasearch.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.vender98.aviasearch.presentation.common.ErrorBus
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

class MainViewModel @Inject constructor(
    application: Application,
    errorBus: ErrorBus,
) : AndroidViewModel(application) {

    val errorsFlow: Flow<String> = errorBus.observe()
    private val _googlePlayServicesInaccessibilityEventsFlow = MutableSharedFlow<Unit>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val googlePlayServicesInaccessibilityEventsFlow = _googlePlayServicesInaccessibilityEventsFlow.asSharedFlow()

    fun checkGooglePlayServicesAvailability() {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        if (googleApiAvailability.isGooglePlayServicesAvailable(getApplication()) != ConnectionResult.SUCCESS) {
            _googlePlayServicesInaccessibilityEventsFlow.tryEmit(Unit)
        }
    }

}