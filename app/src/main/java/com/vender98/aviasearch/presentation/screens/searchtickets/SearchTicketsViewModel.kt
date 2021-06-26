package com.vender98.aviasearch.presentation.screens.searchtickets

import androidx.lifecycle.ViewModel
import com.vender98.aviasearch.ui.screens.searchtickets.Route
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

class SearchTicketsViewModel @Inject constructor(
    private val route: Route
) : ViewModel() {

    private val _routeFlow = MutableSharedFlow<Route>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val routeFlow: Flow<Route> = _routeFlow.asSharedFlow()

    fun onMapReady() {
        _routeFlow.tryEmit(route)
    }
}