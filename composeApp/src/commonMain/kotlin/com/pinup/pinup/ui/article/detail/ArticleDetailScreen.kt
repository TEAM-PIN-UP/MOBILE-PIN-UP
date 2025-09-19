package com.pinup.pinup.ui.article.detail

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.Image
import coil3.compose.AsyncImage
import com.pinup.pinup.domain.model.ArticleDetail
import com.pinup.pinup.domain.model.ArticlePlace
import com.pinup.pinup.extentions.ScrollToEndCallback
import com.pinup.pinup.extentions.clickableWithNoRipple
import com.pinup.pinup.ui.component.ArticleView
import com.pinup.pinup.ui.component.BottomBar
import com.pinup.pinup.ui.component.PHorizontalDivider
import com.pinup.pinup.ui.component.RoundedBox
import com.pinup.pinup.ui.component.TitleBar
import com.pinup.pinup.ui.main.compose.MainDestination
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.Texts
import com.pinup.pinup.ui.theme.Typography
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.ic_bookmark_off
import pinup.composeapp.generated.resources.ic_search

@Composable
fun ArticleDetailScreen(
    detail: ArticleDetail,
    articleList: List<ArticlePlace>,
    onClickPlaceDetail: (String) -> Unit = {},
    onClickScrap: (String) -> Unit = {},
    onClickBack: () -> Unit = {},
) {
    val scrollState = rememberLazyListState()
    val pagerState = rememberPagerState(pageCount = { articleList.size })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Colors.White
            )
    ) {
        TitleBar(
            modifier = Modifier
                .padding(start = 20.dp),
            onLeftButtonClick = onClickBack,
            title = detail.title
        )

        PHorizontalDivider()

        LazyColumn(
            modifier = Modifier
                .background(Colors.White),
            state = scrollState,
        ) {
            item {
                Spacer(modifier = Modifier.height(30.dp))

                Text(
                    modifier = Modifier
                        .padding(start = 20.dp),
                    text = detail.title,
                    color = Colors.Black,
                    style = Typography.D1.copy(
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(25.dp))

                Text(
                    modifier = Modifier
                        .padding(start = 20.dp),
                    text = detail.description,
                    color = Colors.Gray600,
                    style = Typography.B1.copy(
                        fontWeight = FontWeight.Medium
                    )
                )

                Spacer(modifier = Modifier.height(27.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = detail.writer,
                        color = Colors.Gray600,
                        style = Typography.B2.copy(
                            fontWeight = FontWeight.Medium
                        )
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = detail.createdAt,
                        color = Colors.Gray600,
                        style = Typography.B2.copy(
                            fontWeight = FontWeight.Medium
                        )
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                detail.image.forEach {
                    AsyncImage(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .fillMaxWidth(),
                        model = it,
                        contentScale = ContentScale.Crop,
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }

            item {
                Spacer(modifier = Modifier.height(15.dp))

                Text(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth(),
                    text = detail.title,
                    color = Colors.Black,
                    style = Typography.D2.copy(
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(15.dp))
            }

            item {
                detail.place.forEach {
                    Row(
                        modifier = Modifier
                            .clickableWithNoRipple {
                                onClickPlaceDetail(it.kakaoPlaceId)
                            }
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    ) {
                        AsyncImage(
                            modifier = Modifier
                                .size(91.dp),
                            model = it,
                            contentScale = ContentScale.Crop,
                            contentDescription = null
                        )

                        Spacer(modifier = Modifier.width(19.dp))

                        Column {
                            Spacer(modifier = Modifier.height(21.dp))

                            Text(
                                text = it.name,
                                color = Colors.Black,
                                style = Typography.T1.copy(
                                    fontWeight = FontWeight.SemiBold
                                )
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = it.address,
                                color = Colors.Gray500,
                                style = Typography.B2.copy(
                                    fontWeight = FontWeight.Normal
                                )
                            )
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        Image(
                            modifier = Modifier
                                .clickableWithNoRipple {
                                    onClickScrap(it.kakaoPlaceId)
                                },
                            painter = painterResource(Res.drawable.ic_bookmark_off),
                            contentDescription = null
                        )
                    }

                    Spacer(modifier = Modifier.height(15.dp))

                    PHorizontalDivider()
                }
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Colors.Gray800)
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .alpha(0.3f)
                            .fillMaxSize(),
                        model = articleList[pagerState.currentPage].image,
                        contentScale = ContentScale.Crop,
                        contentDescription = null
                    )

                    Column(
                        modifier = Modifier
                            .padding(vertical = 20.dp)
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(start = 20.dp),
                            text = Texts.Article.RECOMMEND_ARTICLE,
                            color = Colors.Gray100,
                            style = Typography.D2.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        HorizontalPager(
                            state = pagerState,
                            contentPadding = PaddingValues(horizontal = 20.dp)
                        ) { page ->
                            Column {
                                AsyncImage(
                                    modifier = Modifier
                                        .height(300.dp)
                                        .width(225.dp),
                                    model = articleList[page].image,
                                    contentScale = ContentScale.Crop,
                                    contentDescription = null
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = articleList[page].title,
                                    color = Colors.Gray200,
                                    style = Typography.T1.copy(
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = articleList[page].description,
                                    color = Colors.Gray200,
                                    style = Typography.B2.copy(
                                        fontWeight = FontWeight.Normal
                                    )
                                )
                            }
                        }



                    }
                }
            }
        }
    }
}