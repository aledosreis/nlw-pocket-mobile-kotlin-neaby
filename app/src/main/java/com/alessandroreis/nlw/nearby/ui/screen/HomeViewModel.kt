package com.alessandroreis.nlw.nearby.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alessandroreis.nlw.nearby.core.network.NearbyRemoteDataSource
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(HomeUIState())
    val uiState: StateFlow<HomeUIState> = _uiState.asStateFlow()

    fun onEvent(event: HomeUIEvent) {
        when(event) {
            HomeUIEvent.OnFetchCategories -> fetchCategories()
            is HomeUIEvent.OnFetchMarkets -> fetchMarkets(categoryId = event.categoryId)
        }
    }

    private fun fetchCategories() {
        viewModelScope.launch {
            _uiState.update { currentUIState ->
                NearbyRemoteDataSource.getCategories().fold(
                    onSuccess = { categories ->
                        currentUIState.copy(
                            categories = categories
                        )
                    },
                    onFailure = { _ ->
                        currentUIState.copy(
                            categories = emptyList()
                        )
                    }
                )
            }
        }
    }

    private fun fetchMarkets(categoryId: String) {
        viewModelScope.launch {
            _uiState.update { currentUIState ->
                NearbyRemoteDataSource.getMarkets(categoryId = categoryId).fold(
                    onSuccess = { markets ->
                        currentUIState.copy(
                            markets = markets,
                            marketLocalizations = markets.map { market ->
                                LatLng(market.latitude, market.longitude)
                            }
                        )
                    },
                    onFailure = { _ ->
                        currentUIState.copy(
                            markets = emptyList(),
                            marketLocalizations = emptyList()
                        )
                    }
                )
            }
        }
    }
}