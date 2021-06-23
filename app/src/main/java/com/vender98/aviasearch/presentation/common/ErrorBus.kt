package com.vender98.aviasearch.presentation.common

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

class ErrorBus @Inject constructor() {

    private val flow = MutableSharedFlow<String>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    fun put(message: String) {
        flow.tryEmit(message)
    }

    fun observe(): Flow<String> = flow

}