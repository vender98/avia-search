package com.vender98.aviasearch.ui.base

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

abstract class BaseFragment(@LayoutRes contentLayoutId: Int) : Fragment(contentLayoutId) {

    private val lifecycleJobs = mutableListOf<Job>()

    override fun onStop() {
        super.onStop()
        lifecycleJobs.forEach(Job::cancel)
        lifecycleJobs.clear()
    }

    fun LifecycleCoroutineScope.launchWhenStartedUntilStop(block: suspend CoroutineScope.() -> Unit) =
        launchWhenStarted(block)
            .also { lifecycleJobs += it }

}