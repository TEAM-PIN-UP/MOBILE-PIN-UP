package com.pinup.pinup.ui.component

import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.PinUPTheme

@Composable
fun PHorizontalDivider(modifier: Modifier = Modifier) {
    HorizontalDivider(
        modifier = modifier,
        thickness = 1.dp,
        color = Colors.Neutral100
    )
}

@Composable
fun PVerticalDivider(modifier: Modifier = Modifier) {
    VerticalDivider(
        modifier = modifier,
        thickness = 1.dp,
        color = Colors.Neutral200
    )
}

@Composable
@Preview
private fun PDividerPreview() {
    PinUPTheme {
        PHorizontalDivider()
    }
}