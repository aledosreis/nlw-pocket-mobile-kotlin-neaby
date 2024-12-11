package com.alessandroreis.nlw.nearby.ui.screen

sealed class MarketDetailsUIEvent {
    data class OnFetchRules(val marketId: String) : MarketDetailsUIEvent()
    data class OnFetchCoupon(val qrCodeContent: String) : MarketDetailsUIEvent()
    data object onResetCoupon : MarketDetailsUIEvent()
}