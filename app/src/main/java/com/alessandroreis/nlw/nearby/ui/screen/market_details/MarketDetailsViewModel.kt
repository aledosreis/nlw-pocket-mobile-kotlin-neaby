package com.alessandroreis.nlw.nearby.ui.screen.market_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alessandroreis.nlw.nearby.core.network.NearbyRemoteDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MarketDetailsViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(MarketDetailsUIState())
    val uiState: StateFlow<MarketDetailsUIState> = _uiState.asStateFlow()

    fun onEvent(event: MarketDetailsUIEvent) {
        when(event) {
            is MarketDetailsUIEvent.OnFetchCoupon -> fetchCoupon(qrCodeContent = event.qrCodeContent)
            is MarketDetailsUIEvent.OnFetchRules -> fetchRules(marketId = event.marketId)
            MarketDetailsUIEvent.onResetCoupon -> resetCoupon()
        }
    }

    private fun fetchCoupon(qrCodeContent: String) {
        viewModelScope.launch {
            NearbyRemoteDataSource.patchCoupon(marketId = qrCodeContent)
                .onSuccess { coupon ->
                    _uiState.update { currentUIState ->
                        currentUIState.copy(
                            coupon = coupon.coupon
                        )
                    }
                }
                .onFailure {
                    _uiState.update { currentUIState ->
                        currentUIState.copy(
                            coupon = ""
                        )
                    }
                }
        }
    }

    private fun fetchRules(marketId: String) {
        viewModelScope.launch {
            NearbyRemoteDataSource.getMarketDetails(marketId = marketId)
                .onSuccess { marketDetails ->
                    _uiState.update { currentUIState ->
                        currentUIState.copy(
                            rules = marketDetails.rules
                        )
                    }
                }
                .onFailure {
                    _uiState.update { currentUIState ->
                        currentUIState.copy(
                            rules = emptyList()
                        )
                    }
                }
        }
    }

    private fun resetCoupon() {
        _uiState.update { currentUIState ->
            currentUIState.copy(
                coupon = null
            )
        }
    }

}