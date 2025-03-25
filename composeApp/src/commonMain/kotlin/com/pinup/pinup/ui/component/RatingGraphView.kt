package com.pinup.pinup.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pinup.pinup.domain.model.RatingGraph
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.Typography

@Composable
fun RatingGraphView(
    ratingGraph: RatingGraph,
    totalReviewCount: Int,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        itemsIndexed(ratingGraph.ratingArray.reversed()) { index, item ->
            val animatedProgress by animateFloatAsState(
                targetValue = (item.toFloat()/totalReviewCount)*100,
                animationSpec = tween(durationMillis = 500),
                label = ""
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                VerticalProgressBar(
                    modifier = Modifier
                        .padding(horizontal = 3.5.dp),
                    progress = animatedProgress
                )

                Text(
                    modifier = Modifier
                        .padding(top = 4.dp),
                    text = "${ratingGraph.ratingArray.size-index}",
                    style = Typography.H6,
                    color = Colors.Neutral800
                )
            }
        }
    }
}

@Composable
fun VerticalProgressBar(
    progress: Float,
    modifier: Modifier = Modifier
) {
    RoundedBox(
        modifier = modifier
            .width(6.dp)
            .height(64.dp),
        backgroundColor = Colors.White,
        cornerRounded = 10,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(progress / 100) // 진행률 적용
                .background(
                    color = Colors.Neutral800,
                    shape = RoundedCornerShape(10.dp)
                )
                .align(Alignment.BottomCenter)
        )
    }
}