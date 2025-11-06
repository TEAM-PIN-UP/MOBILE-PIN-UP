package com.pinup.placePinup.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pinup.placePinup.ui.theme.Colors


@Composable
fun PHorizontalDivider(
    modifier: Modifier = Modifier,
    color: Color = Colors.Gray100
) {
    Divider(
        modifier = modifier,
        thickness = 1.dp,
        color = color
    )
}

@Composable
fun PVerticalDivider(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .width(1.dp)
            .background(Colors.Neutral200),
    )
}
