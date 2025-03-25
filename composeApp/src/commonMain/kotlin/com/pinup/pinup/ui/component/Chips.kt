package com.pinup.pinup.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.pinup.pinup.extentions.clickableWithNoRipple
import com.pinup.pinup.ui.model.ChipState
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.Typography
import org.jetbrains.compose.resources.painterResource

@Composable
fun Chips(
    chipStates: List<ChipState>,
    modifier: Modifier = Modifier,
    onClick: (ChipState) -> Unit = {}
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(chipStates) { chipState ->
            Chip(
                text = chipState.text,
                isSelected = chipState.isSelected,
                icon = chipState.icon?.let { painterResource(it) },
                onClick = { onClick(chipState) }
            )
        }
    }
}

@Composable
fun Chip(
    text: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    icon: Painter? = null,
    selectedColor: Color = Colors.Neutral800,
    unSelectedColor: Color = Colors.Neutral50,
    selectedTextColor: Color = Colors.White,
    unSelectedTextColor: Color = Colors.Neutral800,
    onClick: () -> Unit = {}
) {
    RoundedBox(
        modifier = modifier
            .clickableWithNoRipple {
                onClick()
            },
        cornerRounded = 100,
        backgroundColor = if (isSelected) selectedColor else unSelectedColor,
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 11.dp)
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon?.let {
                Image(
                    modifier = Modifier
                        .padding(end = 4.dp),
                    painter = icon,
                    contentDescription = "chip icon",
                    colorFilter = if (isSelected) ColorFilter.tint(selectedTextColor) else null
                )
            }

            Text(
                text = text,
                style = Typography.H6,
                color = if (isSelected) selectedTextColor else unSelectedTextColor
            )
        }
    }
}