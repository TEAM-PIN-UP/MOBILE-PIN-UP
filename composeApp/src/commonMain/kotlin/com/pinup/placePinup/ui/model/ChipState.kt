package com.pinup.placePinup.ui.model

import com.pinup.placePinup.domain.model.Category
import org.jetbrains.compose.resources.DrawableResource
import pinup.composeapp.generated.resources.*

data class ChipState(
    val icon: DrawableResource?,
    val text: String,
    val isSelected: Boolean,
    val type: Category
) {
    companion object {
        val default = listOf(
            ChipState(
                text = "전체",
                isSelected = true,
                icon = null,
                type = Category.ALL
            ),
            ChipState(
                text = "음식점",
                isSelected = false,
                icon = Res.drawable.ic_food,
                type = Category.RESTAURANT
            ), ChipState(
                text = "카페",
                isSelected = false,
                icon = Res.drawable.ic_cafe,
                type = Category.CAFE
            )
        )
    }
}
