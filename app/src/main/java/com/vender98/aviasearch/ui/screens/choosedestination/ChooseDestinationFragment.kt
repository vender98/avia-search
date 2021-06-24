package com.vender98.aviasearch.ui.screens.choosedestination

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.vender98.aviasearch.R
import com.vender98.aviasearch.databinding.FragmentChooseDestinationBinding
import com.vender98.aviasearch.di.ToothpickViewModelFactory
import com.vender98.aviasearch.domain.City
import com.vender98.aviasearch.presentation.screens.choosedestination.ChooseDestinationViewModel
import com.vender98.aviasearch.ui.base.BaseFragment
import com.vender98.aviasearch.ui.screens.searchtickets.Route
import com.vender98.aviasearch.ui.screens.searchtickets.SearchTicketsFragment
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import reactivecircus.flowbinding.android.widget.textChanges

class ChooseDestinationFragment : BaseFragment(R.layout.fragment_choose_destination) {

    private val binding: FragmentChooseDestinationBinding by viewBinding()
    private val viewModel: ChooseDestinationViewModel by viewModels { ToothpickViewModelFactory() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            departureAutoCompleteTextView.init(viewModel::onDepartureCityInputChanged)
            destinationAutoCompleteTextView.init(viewModel::onDestinationCityInputChanged)
            searchButton.setOnClickListener { viewModel.onSearchButtonClicked() }
        }
    }

    private fun AutoCompleteTextView.init(onInputChanged: (String) -> Unit) {
        setAdapter(generateAutoCompleteAdapter(emptyList()))

        @Suppress("EXPERIMENTAL_API_USAGE")
        textChanges()
            .skipInitialValue()
            .debounce(SEARCH_DELAY_IN_MILLIS)
            .onEach { onInputChanged.invoke(it.toString()) }
            .launchIn(lifecycleScope)
    }

    private fun generateAutoCompleteAdapter(items: List<String>) = ArrayAdapter(
        requireContext(),
        android.R.layout.simple_dropdown_item_1line,
        items
    )

    override fun onStart() {
        super.onStart()
        observeViewModel()
    }

    private fun observeViewModel() {
        lifecycleScope
            .launchWhenStartedUntilStop {
                viewModel.departureCitiesFlow.collect { cities ->
                    updateCities(binding.departureAutoCompleteTextView, cities)
                }
            }
        lifecycleScope
            .launchWhenStartedUntilStop {
                viewModel.destinationCitiesFlow.collect { cities ->
                    updateCities(binding.destinationAutoCompleteTextView, cities)
                }
            }
        lifecycleScope
            .launchWhenStartedUntilStop {
                viewModel.searchButtonStateFlow.collect { updateButton(it) }
            }
        lifecycleScope
            .launchWhenStartedUntilStop {
                viewModel.navigateToSearchTicketsEventsFlow.collect { navigateToSearchTicketsScreen(it) }
            }
    }

    private fun updateCities(autoCompleteTextView: AutoCompleteTextView, cities: List<City>) {
        val adapter = generateAutoCompleteAdapter(cities.map(City::name))
        autoCompleteTextView.setAdapter(adapter)
        adapter.notifyDataSetChanged()
    }

    private fun updateButton(buttonState: SearchButtonState) {
        with(binding) {
            if (buttonState == SearchButtonState.PROGRESS) {
                searchButton.isEnabled = false
                searchButton.text = ""
                searchProgressBar.isVisible = true
            } else {
                searchButton.isEnabled = buttonState == SearchButtonState.ENABLED
                searchButton.text = getString(R.string.choose_destination_search_button)
                searchProgressBar.isVisible = false
            }
        }
    }

    private fun navigateToSearchTicketsScreen(route: Route) {
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, SearchTicketsFragment.newInstance(route))
            .addToBackStack(null)
            .commit()
    }

    companion object {
        private const val SEARCH_DELAY_IN_MILLIS = 300L
    }
}