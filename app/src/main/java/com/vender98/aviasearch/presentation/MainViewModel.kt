package com.vender98.aviasearch.presentation

import androidx.lifecycle.ViewModel
import com.vender98.aviasearch.presentation.common.ErrorBus
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MainViewModel @Inject constructor(
    errorBus: ErrorBus
) : ViewModel() {

    val errorsFlow: Flow<String> = errorBus.observe()

}