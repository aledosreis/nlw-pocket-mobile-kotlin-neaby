package com.alessandroreis.nlw.nearby.ui.screen

sealed class HomeUIEvent {
    data object OnFetchCategories : HomeUIEvent()
    data class OnFetchMarkets(val categoryId: String) : HomeUIEvent()
}