package com.pinup.pinup.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.pinup.pinup.R
import com.pinup.pinup.domain.model.PlaceReview
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.PinUPTheme
import com.pinup.pinup.ui.theme.Typography

@Composable
fun ReviewCard(
    placeReview: PlaceReview,
    modifier: Modifier = Modifier,
    horizontalPadding: Dp = 0.dp
) {
    Column(
        modifier = modifier
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = horizontalPadding)
        ) {
            ReviewedProfileImageView(
                imgUrl = placeReview.writerProfileImageUrl
            )

            Column(
                modifier = Modifier
                    .padding(start = 6.dp)
            ) {
                Text(
                    text = placeReview.writerName,
                    style = Typography.H5,
                    color = Colors.Neutral800,
                )

                Text(
                    modifier = Modifier
                        .padding(top = 3.dp),
                    text = "총 리뷰 ${placeReview.writerTotalReviewCount}",
                    style = Typography.H5,
                    color = Colors.Neutral500,
                )
            }

            Spacer(Modifier.weight(1f))

            Image(
                painter = painterResource(Res.drawable.ic_review_admin),
                contentDescription = "my review admin"
            )
        }

        Row(
            modifier = Modifier
                .padding(horizontal = horizontalPadding)
                .padding(top = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = placeReview.starRating.toString(),
                style = Typography.B4,
                color = Colors.Neutral800
            )

            HalfStarRatingBar(
                modifier = Modifier
                    .padding(start = 2.dp),
                rating = placeReview.starRating.toInt(),
                size = 14.dp,
                spacing = 0.dp
            )

            Text(
                modifier = Modifier
                    .padding(start = 8.dp),
                text = "방문날짜 ${placeReview.visitedDate}",
                style = Typography.C2,
                color = Colors.Neutral500
            )
        }

        Text(
            modifier = Modifier
                .padding(horizontal = horizontalPadding)
                .padding(top = 12.dp),
            text = placeReview.content,
            style = Typography.D2,
            color = Colors.Neutral700
        )

        LazyRow(
            modifier = Modifier
                .padding(start = horizontalPadding)
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(placeReview.reviewImageUrls) {
                ReviewImage(it)
            }
        }
    }
}

@Composable
@Preview
private fun ReviewCardPreview() {
    PinUPTheme {
        ReviewCard(
            placeReview = PlaceReview(
                content = "새우 들어간 딤섬이 젤 마싯음",
                reviewId = 0,
                reviewImageUrls = listOf("","","",""),
                starRating = 4.1,
                visitedDate = "24.10.07",
                writerName = "하니",
                writerProfileImageUrl = "",
                writerTotalReviewCount = 32
            ),
            modifier = Modifier
                .background(color = Colors.White)
        )
    }
}