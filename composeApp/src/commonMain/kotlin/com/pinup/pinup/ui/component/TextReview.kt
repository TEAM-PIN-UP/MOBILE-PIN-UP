package com.pinup.pinup.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pinup.pinup.domain.model.Review
import com.pinup.pinup.extentions.clickableSingleWithNoRipple
import com.pinup.pinup.extentions.clickableWithNoRipple
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.Typography
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.*

@Composable
fun TextReview(
    modifier: Modifier = Modifier,
    review: Review,
    onAdminClick: () -> Unit = {},
    onDetailClick: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .background(Colors.White)
            .padding(vertical = 8.dp, horizontal = 20.dp)
            .clickableSingleWithNoRipple {
                onDetailClick()
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 16.dp),
                text = review.placeName,
                style = Typography.H4,
                color = Colors.Neutral800,
            )

            Image(
                painter = painterResource(Res.drawable.ic_right_arrow),
                contentDescription = "right_arrow"
            )
        }

        PHorizontalDivider()

        Column(
            modifier = Modifier
                .padding(vertical = 16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier,
                    text = "review",
                    style = Typography.H4,
                    color = Colors.Neutral800,
                )

                Image(
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .size(16.dp),
                    painter = painterResource(Res.drawable.ic_star),
                    contentDescription = "rating"
                )

                Text(
                    modifier = Modifier
                        .padding(start = 2.dp),
                    text = review.starRating.toString(),
                    style = Typography.B3,
                    color = Colors.Neutral800,
                )

                Spacer(Modifier.weight(1f))

                Image(
                    modifier = Modifier
                        .clickableWithNoRipple {
                            onAdminClick()
                        },
                    painter = painterResource(Res.drawable.ic_review_admin),
                    contentDescription = "더보기"
                )
            }
            Text(
                modifier = Modifier
                    .padding(top = 4.dp),
                text = review.content,
                style = Typography.B2,
                color = Colors.Neutral700,
            )
        }

        Text(
            modifier = Modifier
                .padding(vertical = 4.dp)
                .align(Alignment.End),
            text = "방문 날짜 ${review.visitedDate}",
            style = Typography.B6,
            color = Colors.Neutral400,
        )
    }
}
