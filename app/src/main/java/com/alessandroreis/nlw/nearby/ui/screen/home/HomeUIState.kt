package com.alessandroreis.nlw.nearby.ui.screen.home

import com.alessandroreis.nlw.nearby.data.model.Category
import com.alessandroreis.nlw.nearby.data.model.Market
import com.google.android.gms.maps.model.LatLng

data class HomeUIState(
    val categories: List<Category>? = null,
    val markets: List<Market>? = null,
    val marketLocalizations: List<LatLng>? = null
)
