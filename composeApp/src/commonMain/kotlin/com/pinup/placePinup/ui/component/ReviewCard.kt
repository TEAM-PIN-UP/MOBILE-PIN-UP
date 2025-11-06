package com.pinup.placePinup.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.*
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.pinup.placePinup.domain.model.PlaceReview
import com.pinup.placePinup.extentions.clickableSingleWithNoRipple
import com.pinup.placePinup.extentions.clickableWithNoRipple
import com.pinup.placePinup.ui.theme.Colors
import com.pinup.placePinup.ui.theme.Typography

@Composable
fun ReviewCard(
    placeReview: PlaceReview,
    modifier: Modifier = Modifier,
    horizontalPadding: Dp = 0.dp,
    onClickMenu: (Int) -> Unit = {},
    onClickLike: (Int, Boolean) -> Unit = {_, _ -> },
    onMovePinlogDetail: (Int) -> Unit = {},
    onMoveUserProfile: (String) -> Unit = {},
) {
    Column(
        modifier = modifier
            .padding(vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(start = horizontalPadding)
        ) {
            ReviewedProfileImageView(
                modifier = Modifier.clickableWithNoRipple {
                    onMoveUserProfile(placeReview.writerName)
                },
                imgUrl = placeReview.writerProfileImageUrl,
                size = 33.dp
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier
                            .clickableWithNoRipple {
                                onMoveUserProfile(placeReview.writerName)
                            },
                        text = placeReview.writerName,
                        style = Typography.B2.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = Colors.Gray800,
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Image(
                        painter = painterResource(Res.drawable.ic_star),
                        contentDescription = "star"
                    )

                    Spacer(modifier = Modifier.width(2.dp))

                    Text(
                        text = placeReview.starRating.toString(),
                        style = Typography.B2.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = Colors.Gray800,
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "${placeReview.visitedDate} 방문",
                        style = Typography.L2.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = Colors.Gray400
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    if (placeReview.isOwn) {
                        Image(
                            modifier = Modifier
                                .clickableSingleWithNoRipple {
                                    onClickMenu(placeReview.reviewId)
                                }
                                .padding(end = 20.dp)
                                .size(20.dp),
                            painter = painterResource(Res.drawable.ic_menu_dot),
                            contentDescription = null
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(placeReview.reviewImageUrls) {
                        ReviewImage(
                            imgUrl = it,
                            modifier = Modifier
                                .size(143.dp),
                            onClickImage = {
                                onMovePinlogDetail(placeReview.reviewId)
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    modifier = Modifier
                        .padding(end = horizontalPadding)
                        .clickableWithNoRipple {
                            onMovePinlogDetail(placeReview.reviewId)
                        },
                    text = placeReview.content,
                    style = Typography.B3.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = Colors.Gray700
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        modifier = Modifier
                            .clickableSingleWithNoRipple{
                                onClickLike(placeReview.reviewId, placeReview.isLikeByUser)
                            },
                        painter = painterResource( if(placeReview.isLikeByUser) Res.drawable.ic_heat_on else Res.drawable.ic_heart_off),
                        contentDescription = "heart"
                    )

                    Spacer(modifier = Modifier.width(3.dp))

                    Text(
                        text = placeReview.likeCount.toString(),
                        style = Typography.L2.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = Colors.Gray800
                    )

                    Spacer(modifier = Modifier.width(14.dp))

                    Image(
                        modifier = Modifier
                            .clickableSingleWithNoRipple{
                                onMovePinlogDetail(placeReview.reviewId)
                            },
                        painter = painterResource(Res.drawable.ic_comment),
                        contentDescription = "comment"
                    )

                    Spacer(modifier = Modifier.width(3.dp))

                    Text(
                        text = placeReview.commentCount.toString(),
                        style = Typography.L2.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = Colors.Gray800
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}