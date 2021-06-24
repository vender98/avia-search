package com.vender98.aviasearch.ui.screens.searchtickets

import androidx.core.os.bundleOf
import com.vender98.aviasearch.R
import com.vender98.aviasearch.ui.base.BaseFragment

class SearchTicketsFragment : BaseFragment(R.layout.activity_main) {

    companion object {
        private const val ROUTE_ARG = "ROUTE_ARG"

        fun newInstance(route: Route) = SearchTicketsFragment().apply {
            arguments = bundleOf(ROUTE_ARG to route)
        }
    }
}