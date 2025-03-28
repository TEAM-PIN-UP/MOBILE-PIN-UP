package com.pinup.pinup.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pinup.pinup.extentions.clickableSingleWithNoRipple
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.Typography

@Composable
fun ReviewedPlaceCard(
    name: String,
    rating: Double,
    distance: String?,
    reviewCount: Int,
    reviewerProfileImageUrls: List<String?>,
    reviewImageUrls: List<String>,
    modifier: Modifier = Modifier,
    onItemClick: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .background(
                color = Colors.White
            )
            .padding(vertical = 20.dp)
            .clickableSingleWithNoRipple {
                onItemClick()
            }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp)
        ) {
            Column {
                Text(
                    text = name,
                    style = Typography.H3,
                    color = Colors.Neutral800
                )

                Row(
                    modifier = Modifier
                        .padding(top = 6.5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(Res.drawable.ic_star),
                        contentDescription = "rating"
                    )

                    Text(
                        modifier = Modifier
                            .padding(start = 2.dp),
                        text = rating.toString(),
                        style = Typography.H4,
                        color = Colors.Neutral800
                    )
                }

                Row(
                    modifier = Modifier
                        .padding(top = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    distance?.let {
                        Text(
                            modifier = Modifier
                                .padding(end = 6.dp),
                            text = it,
                            style = Typography.B4,
                            color = Colors.Neutral500
                        )
                    }
                    Text(
                        text = "리뷰 $reviewCount",
                        style = Typography.B4,
                        color = Colors.Neutral700
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            Row {
                LazyRow {
                    itemsIndexed(reviewerProfileImageUrls) { index, item ->
                        if (index == 3) {
                            ReviewedProfileImageView(
                                modifier = Modifier
                                    .offset(x = (((reviewerProfileImageUrls.size - 1) - index) * 5).dp),
                            ) {
                                Row(
                                    modifier = Modifier
                                        .align(Alignment.Center),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Image(
                                        painter = painterResource(Res.drawable.ic_plus),
                                        contentDescription = null
                                    )

                                    Text(
                                        modifier = Modifier
                                            .padding(start = 1.dp),
                                        text = (reviewerProfileImageUrls.size - 3).toString(),
                                        color = Colors.White,
                                        fontWeight = FontWeight.W500
                                    )
                                }
                            }
                            return@itemsIndexed
                        } else {
                            ReviewedProfileImageView(
                                modifier = Modifier
                                    .offset(x = (((reviewerProfileImageUrls.size - 1) - index) * 5).dp),
                                imgUrl = item
                            )
                        }
                    }
                }
            }
        }

        LazyRow(
            modifier = Modifier
                .padding(top = 16.dp)
                .padding(start = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(reviewImageUrls) {
                ReviewImage(
                    imgUrl = it
                )
            }
        }
    }
}