package com.vender98.aviasearch.ui.base

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import kotlinx.coroutines.Job

abstract class BaseFragment(@LayoutRes contentLayoutId: Int) : Fragment(contentLayoutId) {

    private val lifecycleJobs = mutableListOf<Job>()

    override fun onStop() {
        super.onStop()
        lifecycleJobs.forEach(Job::cancel)
        lifecycleJobs.clear()
    }

    fun Job.untilStop(): Job = this.also { lifecycleJobs += it }

}