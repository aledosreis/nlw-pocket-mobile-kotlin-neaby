package com.alessandroreis.nlw.nearby.data.model

import androidx.annotation.DrawableRes
import com.alessandroreis.nlw.nearby.ui.component.category.CategoryFilterChipView
import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val id: String,
    val name: String
) {
    @get:DrawableRes
    val icon: Int?
        get() = CategoryFilterChipView.Companion.fromDescription(description = name)?.icon
}
