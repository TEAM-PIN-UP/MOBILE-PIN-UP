package com.pinup.placePinup.ui.reviewwrite.successWriteReview
import org.jetbrains.compose.resources.stringResource

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.pinup.placePinup.domain.model.PinlogDetail
import com.pinup.placePinup.extentions.clickableWithNoRipple
import com.pinup.placePinup.ui.component.PButton
import com.pinup.placePinup.ui.component.PHorizontalDivider
import com.pinup.placePinup.ui.component.PagerIndicator
import com.pinup.placePinup.ui.component.ProfileImageView
import com.pinup.placePinup.ui.component.RoundedBox
import com.pinup.placePinup.ui.component.TitleBar
import com.pinup.placePinup.ui.theme.Colors
import com.pinup.placePinup.ui.theme.Typography
import com.pinup.placePinup.util.toShortDateXd
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.*
import pinup.composeapp.generated.resources.ic_right_arrow_300
import pinup.composeapp.generated.resources.ic_star

@Composable
fun PinlogDetailScreen(
    pinlogDetail: PinlogDetail,
    onBackPressed: () -> Unit = {},
    onClickPlaceDetail: (String) -> Unit = {},
) {

    val density = LocalDensity.current
    var bottomBarHeightPx by remember { mutableStateOf(0) }
    val bottomBarHeightDp = with(receiver = density) { bottomBarHeightPx.toDp() }
    val pagerState = rememberPagerState(pageCount = { pinlogDetail.reviewImageUrls.size })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Colors.White)
    ) {
        TitleBar(
            modifier = Modifier
                .padding(start = 20.dp),
            title = stringResource(Res.string.pin_log_detail_title),
            onLeftButtonClick = onBackPressed
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(bottom = bottomBarHeightDp + 20.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth()
                        .clickableWithNoRipple {
                            onClickPlaceDetail(pinlogDetail.kakaoPlaceId)
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                    ) {

                        Text(
                            text = pinlogDetail.placeName,
                            style = Typography.B2.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = Colors.Black,
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "${toShortDateXd(pinlogDetail.visitedDate)} 방문",
                            style = Typography.L2.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = Colors.Gray400,
                        )
                    }

                    Image(
                        painter = painterResource(Res.drawable.ic_right_arrow_300),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(Colors.Gray400)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                PHorizontalDivider()

                Spacer(modifier = Modifier.height(20.dp))
            }

            item {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ProfileImageView(
                        imgUrl = pinlogDetail.writerProfileImageUrl,
                        size = 36.dp,
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        Text(
                            text = pinlogDetail.writerName,
                            style = Typography.B2.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = Colors.Gray900,
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(Res.string.word_pinlog),
                                style = Typography.L2.copy(
                                    fontWeight = FontWeight.Medium
                                ),
                                color = Colors.Gray500,
                            )

                            Spacer(modifier = Modifier.width(2.dp))

                            Text(
                                text = pinlogDetail.authorReviewCount.toString(),
                                style = Typography.L2.copy(
                                    fontWeight = FontWeight.Medium
                                ),
                                color = Colors.Gray800,
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))

                Column {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.ic_star),
                            contentDescription = null
                        )

                        Spacer(modifier = Modifier.width(2.dp))

                        Text(
                            text = pinlogDetail.starRating.toString(),
                            style = Typography.B2.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = Colors.Gray900,
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        Text(
                            text = "${pinlogDetail.createdAt[0]}.${pinlogDetail.createdAt[1]}.${pinlogDetail.createdAt[2]} 작성",
                            style = Typography.L2.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = Colors.Gray400,
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    if (pinlogDetail.reviewImageUrls.isNotEmpty()) {
                        Box {
                            HorizontalPager(
                                state = pagerState,
                            ) { page ->
                                RoundedBox(
                                    modifier = Modifier
                                        .padding(horizontal = 20.dp),
                                    cornerRounded = 8,
                                    backgroundColor = Colors.Gray100,
                                ) {
                                    AsyncImage(
                                        modifier = Modifier
                                            .aspectRatio(1f)
                                            .fillMaxWidth(),
                                        model = pinlogDetail.reviewImageUrls[page],
                                        contentScale = ContentScale.Crop,
                                        contentDescription = "default profile image"
                                    )
                                }
                            }

                            Column(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                            ) {
                                PagerIndicator(
                                    page = pagerState.pageCount,
                                    selectedPage = pagerState.currentPage
                                )

                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        modifier = Modifier
                            .padding(horizontal = 20.dp),
                        text = pinlogDetail.content,
                        style = Typography.B3.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = Colors.Gray700,
                    )
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        PButton(
            modifier = Modifier
                .padding(horizontal = 20.dp),
            text = stringResource(Res.string.word_confirm),
            onClick = {
                onBackPressed()
            }
        )

        Spacer(modifier = Modifier.height(42.dp))
    }
}