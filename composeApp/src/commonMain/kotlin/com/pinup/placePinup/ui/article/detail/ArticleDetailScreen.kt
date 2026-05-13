package com.pinup.placePinup.ui.article.detail
import org.jetbrains.compose.resources.stringResource

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.pinup.placePinup.domain.model.EditorPintsDetail
import com.pinup.placePinup.domain.model.PinchListItem
import com.pinup.placePinup.extentions.clickableWithNoRipple
import com.pinup.placePinup.platform.PlatformWebView
import com.pinup.placePinup.ui.component.PHorizontalDivider
import com.pinup.placePinup.ui.component.TitleBar
import com.pinup.placePinup.ui.theme.Colors
import com.pinup.placePinup.ui.theme.Typography
import kotlinx.coroutines.flow.first
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.*
import pinup.composeapp.generated.resources.ic_bookmark_off
import pinup.composeapp.generated.resources.ic_bookmark_on

@Composable
fun ArticleDetailScreen(
    editorPintsDetail: EditorPintsDetail,
    articleList: List<PinchListItem>,
    savedScrollPosition: Int = 0,
    onClickPlaceDetail: (String) -> Unit = {},
    onClickScrap: (String, Boolean) -> Unit = {_, _ -> },
    onClickArticle: (Int) -> Unit = {},
    onClickBack: () -> Unit = {},
    onSaveLastScrollPosition: (Int) -> Unit = {}
) {
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        snapshotFlow { scrollState.maxValue }
            .first { it >= savedScrollPosition }
        scrollState.scrollTo(savedScrollPosition)
    }

    DisposableEffect(Unit) {
        onDispose {
            onSaveLastScrollPosition(scrollState.value)
        }
    }

    val pagerState = rememberPagerState(pageCount = { articleList.size })
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Colors.White
            )
            .navigationBarsPadding()
    ) {
        TitleBar(
            modifier = Modifier
                .padding(start = 20.dp),
            onLeftButtonClick = onClickBack,
            title = editorPintsDetail.title
        )

        PHorizontalDivider()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Colors.White)
                .verticalScroll(scrollState)
        ) {
            PlatformWebView(
                modifier = Modifier,
                html = editorPintsDetail.content.trimIndent(),
                url = ""
            )

            Spacer(modifier = Modifier.height(15.dp))

            PHorizontalDivider()

            editorPintsDetail.pintsPlaceList.forEach {
                Spacer(modifier = Modifier.height(15.dp))

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
                        model = it.reviewImages[0].url,
                        contentScale = ContentScale.Crop,
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.width(19.dp))

                    Column(
                        modifier = Modifier
                            .weight(1f)
                    ) {
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

                    Image(
                        modifier = Modifier
                            .clickableWithNoRipple {
                                onClickScrap(it.kakaoPlaceId, it.bookmark)
                            },
                        painter = if(it.bookmark) painterResource(Res.drawable.ic_bookmark_on) else painterResource(Res.drawable.ic_bookmark_off),
                        contentDescription = null
                    )
                }

                Spacer(modifier = Modifier.height(15.dp))

                PHorizontalDivider()
            }

            if (articleList.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Colors.Gray800)
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .alpha(0.3f)
                            .matchParentSize(),
                        model = articleList[pagerState.currentPage].imageUrl,
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
                            text = stringResource(Res.string.article_recommend),
                            color = Colors.Gray100,
                            style = Typography.D2.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        BoxWithConstraints(Modifier.fillMaxWidth()) {
                            val itemWidth = 225.dp
                            val startPad = 20.dp
                            val endPad = (maxWidth - itemWidth - startPad).coerceAtLeast(0.dp)
                            HorizontalPager(
                                state = pagerState,
                                modifier = Modifier.fillMaxWidth(),
                                pageSize = PageSize.Fixed(itemWidth),
                                contentPadding = PaddingValues(start = startPad, end = endPad),
                                pageSpacing = 20.dp
                            ) { page ->
                                AsyncImage(
                                    modifier = Modifier
                                        .clickableWithNoRipple {
                                            onClickArticle(articleList[pagerState.currentPage].id)
                                        }
                                        .fillMaxWidth()
                                        .height(300.dp),
                                    model = articleList[page].imageUrl,
                                    contentScale = ContentScale.Crop,
                                    contentDescription = null
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            modifier = Modifier
                                .padding(start = 20.dp),
                            text = articleList[pagerState.currentPage].title,
                            color = Colors.Gray200,
                            style = Typography.T1.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            modifier = Modifier
                                .padding(start = 20.dp),
                            text = articleList[pagerState.currentPage].description,
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