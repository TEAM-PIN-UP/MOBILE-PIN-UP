package com.pinup.pinup.ui.component

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.PinUPTheme

@Composable
fun ReviewImage(
    imgUrl: String,
    modifier: Modifier = Modifier,
) {
    RoundedBox(
        modifier = modifier
            .size(100.dp),
        cornerRounded = 8,
        backgroundColor = Colors.Neutral100,
    ) {
        if (imgUrl.isNotEmpty()) {
            AsyncImage(
                model = imgUrl,
                contentScale = ContentScale.Crop,
                contentDescription = "default profile image"
            )
        }
    }
}

@Composable
@Preview
private fun ReviewImagePreview() {
    PinUPTheme {
        ReviewImage("")
    }
}