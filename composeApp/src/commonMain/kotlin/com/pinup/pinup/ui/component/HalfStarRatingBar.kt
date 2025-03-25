package com.pinup.pinup.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.*
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.pinup.pinup.extentions.clickableWithNoRipple
import com.pinup.pinup.ui.theme.Colors


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