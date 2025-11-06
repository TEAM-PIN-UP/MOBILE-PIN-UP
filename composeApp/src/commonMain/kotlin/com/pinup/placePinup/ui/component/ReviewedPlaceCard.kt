package com.pinup.placePinup.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import com.pinup.placePinup.extentions.clickableSingleWithNoRipple
import com.pinup.placePinup.ui.theme.Colors
import com.pinup.placePinup.ui.theme.Typography

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
        if(reviewImageUrls.size == 1) {
            Spacer(modifier = Modifier.height(20.dp))

            ReviewImage(
                imgUrl = reviewImageUrls[0],
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .aspectRatio(335f/170f)
            )
        }
        else {
            LazyRow(
                modifier = Modifier
                    .padding(top = 20.dp),
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(reviewImageUrls) {
                    ReviewImage(
                        imgUrl = it,
                        modifier = Modifier
                            .size(width = 300.dp, height = 170.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = name,
                style = Typography.B1.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = Colors.Gray800
            )

            Spacer(modifier = Modifier.width(6.dp))

            distance?.let {
                Text(
                    modifier = Modifier
                        .padding(end = 6.dp),
                    text = it,
                    style = Typography.B4,
                    color = Colors.Gray400
                )
            }
        }

        Spacer(modifier = Modifier.height(5.dp))

        Row(
            modifier = Modifier.padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(Res.drawable.ic_star),
                contentDescription = "rating"
            )

            Spacer(modifier = Modifier.width(2.dp))

            Text(
                text = rating.toString(),
                style = Typography.B2.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = Colors.Gray800
            )

            Spacer(modifier = Modifier.width(2.dp))

            Text(
                text = "($reviewCount)",
                style = Typography.L1.copy(
                    fontWeight = FontWeight.Normal
                ),
                color = Colors.Gray500
            )

            Spacer(Modifier.weight(1f))

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
}