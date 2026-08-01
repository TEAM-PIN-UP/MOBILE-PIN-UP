package com.pinup.placePinup.ui.component
import org.jetbrains.compose.resources.stringResource

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.pinup.placePinup.domain.model.Review
import com.pinup.placePinup.extentions.clickableWithNoRipple
import com.pinup.placePinup.ui.theme.Colors
import com.pinup.placePinup.ui.theme.Texts
import com.pinup.placePinup.ui.theme.Texts.buildHighlightedText
import com.pinup.placePinup.ui.theme.Typography
import com.pinup.placePinup.util.relativeOrDate
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.*
import pinup.composeapp.generated.resources.ic_comment
import pinup.composeapp.generated.resources.ic_heart_off
import pinup.composeapp.generated.resources.ic_heat_on
import pinup.composeapp.generated.resources.ic_map_on
import pinup.composeapp.generated.resources.ic_menu_dot

@Composable
fun FeedView(
    item: Review,
    searchKeyWord: String = "",
    onClickMenu: (Int) -> Unit = {},
    onClickLike: (Int, Boolean) -> Unit = {_, _ -> },
    onClickDetail: (Int) -> Unit = {},
    onClickPlace: (String) -> Unit = {},
    onMoveUserProfile: (String) -> Unit = {}
) {
    var isOverflow by remember { mutableStateOf(false) }
    val pagerState = rememberPagerState(pageCount = { item.reviewImageUrls?.size ?: 0})

    Column(
        modifier = Modifier
            .background(Colors.White)
            .padding(bottom = 20.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .clickableWithNoRipple{
                    onMoveUserProfile(item.writerName)
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProfileImageView(
                modifier = Modifier,
                imgUrl = item.writerProfileImageUrl,
                size = 36.dp,
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    modifier = Modifier,
                    text = item.writerName,
                    style = Typography.B2.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = Colors.Gray900,
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = relativeOrDate(item.createdAt),
                    style = Typography.L2.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = Colors.Gray500,
                )
            }

            if (item.isOwn) {
                Image(
                    modifier = Modifier
                        .clickableWithNoRipple {
                            onClickMenu(item.id)
                        },
                    painter = painterResource(Res.drawable.ic_menu_dot),
                    contentDescription = null,
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        RoundedBox(
            modifier = Modifier
                .padding(start = 20.dp)
                .clickableWithNoRipple {
                    if (item.kakaoPlaceId.isNotBlank()) onClickPlace(item.kakaoPlaceId)
                },
            cornerColor = Colors.Main,
        ) {
            Row(
                modifier = Modifier
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .size(12.dp),
                    painter = painterResource(Res.drawable.ic_map_on),
                    contentDescription = null,
                )

                Spacer(modifier = Modifier.width(2.dp))

                Text(
                    text = item.placeName,
                    style = Typography.L2.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = Colors.Main,
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (!item.reviewImageUrls.isNullOrEmpty()) {
            Box {
                HorizontalPager(
                    state = pagerState,
                ) { page ->
                    RoundedBox(
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                    ) {
                        AsyncImage(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .fillMaxWidth()
                                .clickableWithNoRipple {
                                    onClickDetail(item.id)
                                },
                            model = item.reviewImageUrls[page],
                            contentScale = ContentScale.Crop,
                            contentDescription = null
                        )
                    }
                }

                if (item.reviewImageUrls.size != 1) {
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
        }

        Text(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .clickableWithNoRipple {
                    onClickDetail(item.id)
                },
            text = buildHighlightedText(item.content, keyword = searchKeyWord, Colors.Main),
            style = Typography.B3.copy(
                fontWeight = FontWeight.Medium
            ),
            color = Colors.Gray700,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = { result ->
                isOverflow = result.lineCount > 2
            }
        )

        if (isOverflow) {
           Text(
               modifier = Modifier
                   .padding(horizontal = 20.dp)
                   .clickableWithNoRipple {
                       onClickDetail(item.id)
                   },
               text = stringResource(Res.string.word_see_more),
               style = Typography.B3.copy(
                   fontWeight = FontWeight.Medium
               ),
               color = Colors.Gray400,
           )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .clickableWithNoRipple {
                        onClickLike(item.id, item.isLikedByUser)
                    },
                painter = painterResource(if (item.isLikedByUser) Res.drawable.ic_heat_on else Res.drawable.ic_heart_off),
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(3.dp))

            Text(
                text = item.likeCount.toString(),
                style = Typography.L2.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = Colors.Gray800,
            )

            Spacer(modifier = Modifier.width(14.dp))

            Image(
                modifier = Modifier
                    .clickableWithNoRipple {
                        onClickDetail(item.id)
                    },
                painter = painterResource(Res.drawable.ic_comment),
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = item.commentCount.toString(),
                style = Typography.L2.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = Colors.Gray800,
            )
        }
    }
}