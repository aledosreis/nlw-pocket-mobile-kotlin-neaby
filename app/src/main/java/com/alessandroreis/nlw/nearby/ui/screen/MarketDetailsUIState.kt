package com.alessandroreis.nlw.nearby.ui.screen

import com.alessandroreis.nlw.nearby.data.model.Rule

data class MarketDetailsUIState(
    val rules: List<Rule>? = null,
    val coupon: String? = null
)
