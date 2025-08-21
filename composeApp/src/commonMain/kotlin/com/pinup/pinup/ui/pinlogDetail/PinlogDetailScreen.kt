package com.pinup.pinup.ui.pinlogDetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.pinup.pinup.ui.component.CommentView
import com.pinup.pinup.ui.component.PHorizontalDivider
import com.pinup.pinup.ui.component.PagerIndicator
import com.pinup.pinup.ui.component.ProfileImageView
import com.pinup.pinup.ui.component.RoundedBox
import com.pinup.pinup.ui.component.TitleBar
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.Texts
import com.pinup.pinup.ui.theme.Typography
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.ic_bookmark_off
import pinup.composeapp.generated.resources.ic_bookmark_on
import pinup.composeapp.generated.resources.ic_comment
import pinup.composeapp.generated.resources.ic_heart_off
import pinup.composeapp.generated.resources.ic_heat_on
import pinup.composeapp.generated.resources.ic_menu_dot
import pinup.composeapp.generated.resources.ic_right_arrow_300
import pinup.composeapp.generated.resources.ic_star

@Composable
fun PinlogDetailScreen(
    placeName: String = "우동 카덴",
    visitDate: String = "25.06.22",
    profileImage: String = "https://lh3.googleusercontent.com/d/1O90AKH6CG243YwWTAmXKMtqNI3cVV0Lu",
    userName: String = "닉네임",
    reviewCount: Int = 24,
    starRating: Double = 4.5,
    createdDate: String = "25.07.14",
    reviewImageUrls: List<String> = listOf(
        "https://lh3.googleusercontent.com/d/1YRKXQv5YHJKz4cD-8efcRaKQ1ENnN9U7",
        "https://lh3.googleusercontent.com/d/1Wp8SvCGWnliHUfIrQEpTFyJKae7axrBc",
        "https://lh3.googleusercontent.com/d/1XY6r4cmUqtNPEImG9ZmqxzOfOHbmCaxX"
    ),
    content: String = "이건 테스트용 글입니다.",
    isOwn: Boolean = true,
    likeCount: Int = 100,
    isLikedByUse: Boolean = false,
    commentCount: Int = 100,
    comments: List<String> = emptyList(),
    isScrapByUser: Boolean = false,
    onBackPressed: () -> Unit = {},
) {

    val pagerState = rememberPagerState(pageCount = { reviewImageUrls.size })

    Column(
        modifier = Modifier
            .fillMaxSize()
    ){
        TitleBar(
            modifier = Modifier
                .padding(start = 20.dp),
            title = Texts.PinLog.DETAIL_TITLE,
            onLeftButtonClick = onBackPressed
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                    ) {

                        Text(
                            text = placeName,
                            style = Typography.B2.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = Colors.Black,
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "$visitDate 방문",
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
                        imgUrl = profileImage,
                        size = 36.dp,
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        Text(
                            text = userName,
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
                                text = Texts.Word.PINLOG,
                                style = Typography.L2.copy(
                                    fontWeight = FontWeight.Medium
                                ),
                                color = Colors.Gray500,
                            )

                            Spacer(modifier = Modifier.width(2.dp))

                            Text(
                                text = reviewCount.toString(),
                                style = Typography.L2.copy(
                                    fontWeight = FontWeight.Medium
                                ),
                                color = Colors.Gray800,
                            )
                        }
                    }

                    if (isOwn) {
                        Image(
                            painter = painterResource(Res.drawable.ic_menu_dot),
                            contentDescription = null,
                        )
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
                            text = starRating.toString(),
                            style = Typography.B2.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = Colors.Gray900,
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        Text(
                            text = "$createdDate 작성",
                            style = Typography.L2.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = Colors.Gray400,
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    if (reviewImageUrls.isNotEmpty()) {
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
                                        model = reviewImageUrls[page],
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
                        text = content,
                        style = Typography.B3.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = Colors.Gray700,
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(if (isLikedByUse) Res.drawable.ic_heat_on else Res.drawable.ic_heart_off),
                            contentDescription = null
                        )

                        Spacer(modifier = Modifier.width(3.dp))

                        Text(
                            text = likeCount.toString(),
                            style = Typography.L2.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = Colors.Gray800,
                        )

                        Spacer(modifier = Modifier.width(14.dp))

                        Image(
                            painter = painterResource(Res.drawable.ic_comment),
                            contentDescription = null
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = commentCount.toString(),
                            style = Typography.L2.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = Colors.Gray800,
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        Image(
                            modifier = Modifier
                                .size(24.dp),
                            painter = painterResource(if (isScrapByUser) Res.drawable.ic_bookmark_on else Res.drawable.ic_bookmark_off),
                            contentDescription = null
                        )
                    }
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(50.dp))

                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = Texts.Word.COMMENT,
                        style = Typography.T2.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = Colors.Gray800,
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = commentCount.toString(),
                        style = Typography.T2.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = Colors.Gray800,
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                PHorizontalDivider()

                Spacer(modifier = Modifier.height(20.dp))
            }

            items(comments){
                CommentView()

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Preview
@Composable
fun Test(){
    PinlogDetailScreen()
}