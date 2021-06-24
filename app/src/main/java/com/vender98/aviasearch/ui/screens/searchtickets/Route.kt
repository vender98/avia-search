package com.vender98.aviasearch.ui.screens.searchtickets

import android.os.Parcelable
import com.vender98.aviasearch.domain.City
import kotlinx.parcelize.Parcelize

@Parcelize
data class Route(
    val departureCity: City,
    val destinationCity: City
) : Parcelable
