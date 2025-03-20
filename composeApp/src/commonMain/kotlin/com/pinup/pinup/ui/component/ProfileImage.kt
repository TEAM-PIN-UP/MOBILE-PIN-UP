package com.pinup.pinup.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.pinup.pinup.R
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.PinUPTheme

@Composable
fun ReviewedProfileImageView(
    modifier: Modifier = Modifier,
    size: Dp = 26.dp,
    content: @Composable BoxScope.() -> Unit,
) {
    RoundedBox(
        modifier = modifier
            .size(size),
        cornerRounded = 100,
        cornerColor = Colors.White,
        backgroundColor = Colors.Neutral400,
    ) {
        content()
    }
}

@Composable
fun ReviewedProfileImageView(
    imgUrl: String?,
    modifier: Modifier = Modifier,
    size: Dp = 26.dp,
) {
    if (imgUrl.isNullOrEmpty()) {
        RoundedBox(
            modifier = modifier
                .size(size),
            cornerRounded = 100,
            cornerColor = Colors.White
        ) {
            Image(
                painter = painterResource(Res.drawable.ic_kakao),
                contentDescription = "default profile image"
            )
        }
    } else {
        RoundedBox(
            modifier = modifier
                .size(size),
            cornerRounded = 100,
            cornerColor = Colors.White
        ) {
            AsyncImage(
                model = imgUrl,
                contentDescription = "default profile image"
            )
        }
    }
}

@Composable
fun ProfileImageView(
    imgUrl: String?,
    modifier: Modifier = Modifier,
    size: Dp = 26.dp,
    cornerColor: Color = Colors.Transparency,
) {
    if (imgUrl.isNullOrEmpty()) {
        RoundedBox(
            modifier = modifier
                .size(size),
            cornerRounded = 100,
            cornerColor = cornerColor,
        ) {
            Image(
                painter = painterResource(Res.drawable.ic_kakao),
                contentDescription = "default profile image"
            )
        }
    } else {
        RoundedBox(
            modifier = modifier
                .size(size),
            cornerRounded = 100,
            cornerColor = cornerColor,
        ) {
            AsyncImage(
                model = imgUrl,
                contentDescription = "default profile image"
            )
        }
    }
}

@Composable
@Preview
private fun ProfileImageViewPreview() {
    PinUPTheme {
        ReviewedProfileImageView("")
    }
}