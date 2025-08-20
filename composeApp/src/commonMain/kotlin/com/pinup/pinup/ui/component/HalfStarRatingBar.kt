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
import com.pinup.pinup.ui.theme.Colors
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import com.pinup.pinup.platform.hLog


@Composable
fun HalfStarRatingBar(
    rating: Float,                           // 0.0 ~ maxStars, 0.5 단위
    modifier: Modifier = Modifier,
    maxStars: Int = 5,
    size: Dp = 28.dp,
    spacing: Dp = 8.dp,
    onRatingChanged: (Float) -> Unit = {}
) {
    val density = LocalDensity.current
    val sizePxFallback = with(density) { size.toPx() }

    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spacing)
    ) {
        items(maxStars) { index ->
            val full = rating >= index + 1f
            val half = !full && rating >= index + 0.5f

            var widthPx by remember { mutableStateOf(0f) }

            val painter = when {
                full -> painterResource(Res.drawable.ic_star_off)
                half -> painterResource(Res.drawable.ic_star_half)
                else -> painterResource(Res.drawable.ic_star_off)
            }

            hLog("full : $full, half : $half, myRating : $rating, index : $index")

            Image(
                modifier = Modifier
                    .size(size)
                    .onSizeChanged { widthPx = it.width.toFloat() }
                    .pointerInput(widthPx) {
                        detectTapGestures { offset ->
                            val w = if (widthPx > 0f) widthPx else sizePxFallback
                            val isRight = offset.x >= w / 2f
                            val newRating = index + if (isRight) 1f else 0.5f
                            onRatingChanged(newRating.coerceIn(0.5f, maxStars.toFloat()))
                        }
                    },
                painter = painter,
                contentDescription = "star",
                colorFilter = when {
                    full -> ColorFilter.tint(Colors.Main)
                    else -> null
                } // 채운 상태 컬러 else -> null },
            )
        }
    }
}
