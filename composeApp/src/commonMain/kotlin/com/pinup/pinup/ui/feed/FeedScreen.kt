package com.pinup.pinup.ui.feed

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pinup.pinup.domain.model.Review
import com.pinup.pinup.extentions.ScrollToEndCallback
import com.pinup.pinup.extentions.clickableWithNoRipple
import com.pinup.pinup.ui.component.BottomBar
import com.pinup.pinup.ui.component.FeedView
import com.pinup.pinup.ui.component.PHorizontalDivider
import com.pinup.pinup.ui.component.PinlogMenuBottomSheet
import com.pinup.pinup.ui.main.compose.MainDestination
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.Texts
import com.pinup.pinup.ui.theme.Typography
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.ic_search

@Composable
fun FeedScreen(
    reviewList: List<Review>,
    profile: String,
    getMoreFeed: () -> Unit = {},
    onClickBottomNav: (MainDestination) -> Unit,
    onClickSearch: () -> Unit,
    onClickEdit: (Int) -> Unit = {},
    onClickDelete: (Int) -> Unit = {},
    onClickDetail: (Int) -> Unit = {},
    onClickLike: (Int, Boolean) -> Unit = {_, _ -> },
) {
    val scope: CoroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        ModalBottomSheetValue.Hidden
    )
    var clickedReviewId by remember { mutableStateOf(0) }
    val scrollState = rememberLazyListState()

    ScrollToEndCallback(scrollState) {
        getMoreFeed()
    }

    ModalBottomSheetLayout(
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetContent = {
            PinlogMenuBottomSheet(
                onClickEdit = {
                    onClickEdit(clickedReviewId)
                    scope.launch { sheetState.hide() }
                },
                onClickDelete = {
                    onClickDelete(clickedReviewId)
                    scope.launch { sheetState.hide() }
                },
            )
        },
        sheetBackgroundColor = Colors.White,
        sheetState = sheetState,
    ) {
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
                    text = Texts.Word.FEED,
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

            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .background(Colors.Gray50),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                state = scrollState
            ) {
                items(reviewList){
                    FeedView(
                        item = it,
                        onClickMenu = {
                            clickedReviewId = it
                            scope.launch { sheetState.show() }
                        },
                        onClickLike = onClickLike,
                        onClickDetail = onClickDetail,
                        onClickScrap = {},
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ){
            BottomBar(
                selectedMenu = MainDestination.Feed,
                profileImage = profile,
                onBottomMenuClick = onClickBottomNav
            )
        }
    }
}