package com.pinup.placePinup.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.*
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.pinup.placePinup.ui.theme.Colors

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
    if (imgUrl.isNullOrEmpty() || !imgUrl.startsWith("http")) {
        RoundedBox(
            modifier = modifier
                .size(size),
            cornerRounded = 100,
            cornerColor = Colors.White
        ) {
            Image(
                painter = painterResource(Res.drawable.ic_profile_default),
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
                contentScale = ContentScale.Crop,
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
    if (imgUrl.isNullOrEmpty() || !imgUrl.startsWith("http")) {
        RoundedBox(
            modifier = modifier
                .size(size),
            cornerRounded = 100,
            cornerColor = cornerColor,
        ) {
            Image(
                painter = painterResource(Res.drawable.ic_profile_default),
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
                contentScale = ContentScale.Crop,
                contentDescription = "profile image"
            )
        }
    }
}