package com.vender98.aviasearch.ui.screens.choosedestination

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.vender98.aviasearch.R
import com.vender98.aviasearch.di.ToothpickViewModelFactory
import com.vender98.aviasearch.presentation.screens.choosedestination.ChooseDestinationViewModel

class ChooseDestinationFragment : Fragment(R.layout.fragment_choose_destination) {

    private val viewModel: ChooseDestinationViewModel by viewModels { ToothpickViewModelFactory() }

}