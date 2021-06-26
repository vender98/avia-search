package com.vender98.aviasearch.presentation.screens.searchtickets

import androidx.lifecycle.ViewModel
import com.vender98.aviasearch.ui.screens.searchtickets.Route
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import javax.inject.Inject

class SearchTicketsViewModel @Inject constructor(
    private val route: Route
) : ViewModel() {

    private val _routeFlow = MutableStateFlow<Route?>(null)
    val routeFlow: Flow<Route> = _routeFlow.asStateFlow().filterNotNull()

    init {
        _routeFlow.tryEmit(route)
    }
}