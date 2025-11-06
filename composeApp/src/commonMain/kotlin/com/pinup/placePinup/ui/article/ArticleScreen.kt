package com.pinup.placePinup.ui.article

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pinup.placePinup.domain.model.PinchListItem
import com.pinup.placePinup.extentions.ScrollToEndCallback
import com.pinup.placePinup.extentions.clickableWithNoRipple
import com.pinup.placePinup.ui.component.ArticleView
import com.pinup.placePinup.ui.component.BottomBar
import com.pinup.placePinup.ui.component.PHorizontalDivider
import com.pinup.placePinup.ui.main.compose.MainDestination
import com.pinup.placePinup.ui.theme.Colors
import com.pinup.placePinup.ui.theme.Texts
import com.pinup.placePinup.ui.theme.Typography
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.ic_search

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ArticleScreen(
    articleList: List<PinchListItem>,
    profile: String,
    getMoreArticle: () -> Unit = {},
    onClickBottomNav: (MainDestination) -> Unit,
    onClickSearch: () -> Unit,
    onClickDetail: (Int) -> Unit = {},
    onRefresh: () -> Unit = {},
    isRefreshing: Boolean = false,
) {
    val scrollState = rememberLazyListState()
    var bottomBarHeight by remember { mutableStateOf(0.dp) }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = onRefresh
    )

    ScrollToEndCallback(scrollState) {
        getMoreArticle()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Colors.White
            )
    ) {
        Row(
            modifier = Modifier
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = Texts.Article.TITLE,
                style = Typography.T1.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = Colors.Gray800
            )

            Spacer(modifier = Modifier.weight(1f))

            Image(
                modifier = Modifier
                    .clickableWithNoRipple {
                        onClickSearch()
                    },
                painter = painterResource(Res.drawable.ic_search),
                contentDescription = null,
            )
        }

        PHorizontalDivider()

        if (articleList.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = Texts.FEED.EMPTY_FEED,
                    color = Colors.Gray400,
                    style = Typography.B1.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        } else {
            Box(
                Modifier
                    .pullRefresh(
                        state = pullRefreshState,
                    )
            ) {
                LazyColumn(
                    modifier = Modifier
                        .padding(bottom = bottomBarHeight)
                        .background(Colors.White),
                    state = scrollState,
                ) {
                    item {
                        Spacer(
                            modifier = Modifier
                                .height(24.dp)
                        )
                    }

                    items(articleList) {
                        ArticleView(
                            modifier = Modifier
                                .padding(horizontal = 20.dp),
                            item = it,
                            onClickItem = onClickDetail,
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                PullRefreshIndicator(
                    refreshing = isRefreshing,
                    state = pullRefreshState,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        BottomBar(
            selectedMenu = MainDestination.Article,
            profileImage = profile,
            onBottomMenuClick = onClickBottomNav,
            onSizeChanged = { bottomBarHeight = it }
        )
    }

}