package com.pinup.placePinup.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pinup.placePinup.ui.theme.Colors

@Composable
fun RoundedBox(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Colors.White,
    cornerColor: Color = Colors.Transparency,
    cornerRounded: Int = 8,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .border(
                width = 1.dp,
                color = cornerColor,
                shape = RoundedCornerShape(cornerRounded.dp)
            )
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(cornerRounded.dp)
            )
            .clip(
                shape = RoundedCornerShape(cornerRounded.dp)
            ),
    ) {
        content()
    }
}