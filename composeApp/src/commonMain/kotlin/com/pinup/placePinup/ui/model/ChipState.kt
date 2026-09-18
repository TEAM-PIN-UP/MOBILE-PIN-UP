package com.pinup.placePinup.ui.model

import com.pinup.placePinup.domain.model.Category
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import pinup.composeapp.generated.resources.*

data class ChipState(
    val icon: DrawableResource?,
    val text: String = "",
    val textRes: StringResource? = null,
    val isSelected: Boolean,
    val type: Category
) {
    companion object {
        val default = listOf(
            ChipState(
                textRes = Res.string.category_all,
                isSelected = true,
                icon = null,
                type = Category.ALL
            ),
            ChipState(
                textRes = Res.string.category_restaurant,
                isSelected = false,
                icon = Res.drawable.ic_food,
                type = Category.RESTAURANT
            ), ChipState(
                textRes = Res.string.category_cafe,
                isSelected = false,
                icon = Res.drawable.ic_cafe,
                type = Category.CAFE
            )
        )
    }
}
