package com.vender98.aviasearch.presentation.screens.choosedestination

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vender98.aviasearch.domain.City
import com.vender98.aviasearch.presentation.common.ErrorHandler
import com.vender98.aviasearch.repository.CityRepository
import com.vender98.aviasearch.ui.screens.choosedestination.SearchButtonState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChooseDestinationViewModel @Inject constructor(
    private val errorHandler: ErrorHandler,
    private val cityRepository: CityRepository
) : ViewModel() {

    private val _departureCitiesFlow = MutableStateFlow<List<City>>(emptyList())
    val departureCitiesFlow = _departureCitiesFlow.asStateFlow()

    private val _destinationCitiesFlow = MutableStateFlow<List<City>>(emptyList())
    val destinationCitiesFlow = _destinationCitiesFlow.asStateFlow()

    private val _searchButtonStateFlow = MutableStateFlow(SearchButtonState.DISABLED)
    val searchButtonStateFlow = _searchButtonStateFlow.asStateFlow()

    private var departureCityInput: String = ""
    private var destinationCityInput: String = ""

    fun onDepartureCityInputChanged(input: String) {
        if (input != departureCityInput) {
            onCityInputChanged(_departureCitiesFlow, input)
            departureCityInput = input
        }
    }

    fun onDestinationCityInputChanged(input: String) {
        if (input != destinationCityInput) {
            onCityInputChanged(_destinationCitiesFlow, input)
            destinationCityInput = input
        }
    }

    private fun onCityInputChanged(cityFlow: MutableStateFlow<List<City>>, input: String) {
        _searchButtonStateFlow.value = SearchButtonState.PROGRESS
        viewModelScope.launch {
            try {
                val cities = cityRepository.searchCities(input)
                cityFlow.value = cities
                checkSearchButtonAvailability()
            } catch (error: Throwable) {
                errorHandler.proceed(error)
                _searchButtonStateFlow.value = SearchButtonState.DISABLED
            }
        }
    }

    private fun checkSearchButtonAvailability() {
        val isButtonEnabled =
            _departureCitiesFlow.value.any { it.name.equals(departureCityInput, ignoreCase = true) }
                    && _destinationCitiesFlow.value.any { it.name.equals(destinationCityInput, ignoreCase = true) }
                    && !departureCityInput.equals(destinationCityInput, ignoreCase = true)
        _searchButtonStateFlow.value = if (isButtonEnabled) SearchButtonState.ENABLED else SearchButtonState.DISABLED
    }

    fun onSearchButtonClicked() {
        // TODO: navigate to the "Search tickets" screen
    }
}