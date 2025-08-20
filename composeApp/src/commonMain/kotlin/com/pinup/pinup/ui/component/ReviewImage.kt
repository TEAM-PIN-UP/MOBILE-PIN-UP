package com.pinup.pinup.ui.component

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale

import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.pinup.pinup.extentions.clickableWithNoRipple
import com.pinup.pinup.ui.theme.Colors


@Composable
fun ReviewImage(
    imgUrl: ByteArray,
    modifier: Modifier = Modifier,
    onClickImage: (ByteArray) -> Unit = {},
) {
    RoundedBox(
        modifier = modifier
            .size(100.dp)
            .clickableWithNoRipple{
                onClickImage(imgUrl)
            },
        cornerRounded = 8,
        backgroundColor = Colors.Gray100,
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
fun ReviewImage(
    imgUrl: String,
    modifier: Modifier = Modifier,
    onClickImage: (String) -> Unit = {},
) {
    RoundedBox(
        modifier = modifier
            .clickableWithNoRipple{
                onClickImage(imgUrl)
            },
        cornerRounded = 8,
        backgroundColor = Colors.Gray100,
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