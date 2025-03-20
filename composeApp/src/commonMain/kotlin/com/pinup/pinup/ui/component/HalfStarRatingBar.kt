package com.pinup.pinup.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.pinup.pinup.R
import com.pinup.pinup.extentions.clickableWithNoRipple
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.PinUPTheme


@Composable
fun HalfStarRatingBar(
    rating: Int,
    modifier: Modifier = Modifier,
    maxStars: Int = 5,
    size: Dp = 28.dp,
    spacing: Dp = 8.dp,
    onRatingChanged: (Int) -> Unit = {}
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spacing)
    ) {
        items(maxStars) { index ->
            Image(
                modifier = Modifier
                    .size(size)
                    .clickableWithNoRipple {
                        onRatingChanged(index+1)
                    },
                painter = painterResource(Res.drawable.ic_star_off),
                contentDescription = "star",
                colorFilter = if (rating > index) {
                    ColorFilter.tint(Colors.Neutral800)
                } else {
                    null
                }
            )
        }
    }
}

@Preview
@Composable
private fun HalfStarRatingBarPreview() {
    PinUPTheme {
        HalfStarRatingBar(
            rating = 3
        ) { }
    }
}
