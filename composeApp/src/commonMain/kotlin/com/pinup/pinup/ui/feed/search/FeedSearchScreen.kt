package com.pinup.pinup.ui.feed.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.pinup.pinup.domain.model.Review
import com.pinup.pinup.extentions.ScrollToEndCallback
import com.pinup.pinup.extentions.clickableWithNoRipple
import com.pinup.pinup.platform.hLog
import com.pinup.pinup.ui.component.BottomBar
import com.pinup.pinup.ui.component.FeedView
import com.pinup.pinup.ui.component.PHorizontalDivider
import com.pinup.pinup.ui.component.PinlogMenuBottomSheet
import com.pinup.pinup.ui.component.RoundedTextField
import com.pinup.pinup.ui.main.compose.MainDestination
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.Texts
import com.pinup.pinup.ui.theme.Typography
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.ic_back
import pinup.composeapp.generated.resources.ic_cancel_300
import pinup.composeapp.generated.resources.ic_close
import pinup.composeapp.generated.resources.ic_search

@Composable
fun FeedSearchScreen(
    query: String,
    profile: String,
    reviewList: List<Review>,
    recentSearchList: List<String>,
    onValueChange: (String) -> Unit,
    onClickBack: () -> Unit = {},
    onClickSearch: () -> Unit = {},
    getMoreFeed: () -> Unit = {},
    onClickBottomNav: (MainDestination) -> Unit,
    onClickDeleteRecentSearch: (Int) -> Unit = {},
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
    val keyboardController = LocalSoftwareKeyboardController.current

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
                .padding(horizontal = 20.dp)
                .statusBarsPadding(),
        ) {
            Row(
                modifier = Modifier
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .clickableWithNoRipple {
                            onClickBack()
                        },
                    painter = painterResource(Res.drawable.ic_back),
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(8.dp))

                RoundedTextField(
                    modifier = Modifier
                        .weight(1f),
                    text = query,
                    textStyle = Typography.B1.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    textColor = Colors.Gray800,
                    singleLine = true,
                    onValueChange = onValueChange,
                    cornerRounded = 999,
                    tailIcon = if (query.isNotEmpty()) painterResource(Res.drawable.ic_close) else null,
                    tailIconSize = 20,
                    onTailIconClick = {
                        onValueChange("")
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            onClickSearch()
                            keyboardController?.hide()
                        },
                    ),
                    fixedBorderColor = Colors.Gray300,
                    backgroundColor = Colors.White,
                    contentPadding = PaddingValues(12.dp),
                )

                Spacer(modifier = Modifier.width(8.dp))

                Image(
                    modifier = Modifier
                        .clickableWithNoRipple {
                            onClickSearch()
                        },
                    painter = painterResource(Res.drawable.ic_search),
                    contentDescription = null
                )
            }

            PHorizontalDivider()

            Spacer(modifier = Modifier.height(16.dp))

            if (query.isEmpty()) {
                Column {
                    Text(
                        text = Texts.FEED.RECENT_SEARCH,
                        color = Colors.Gray800,
                        style = Typography.B2.copy(
                            fontWeight = FontWeight.Medium
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    recentSearchList.forEachIndexed { index, text ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                modifier = Modifier
                                    .clickableWithNoRipple {
                                        onValueChange(text)
                                    },
                                text = text,
                                color = Colors.Gray700,
                                style = Typography.B1.copy(
                                    fontWeight = FontWeight.Medium
                                )
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            Image(
                                modifier = Modifier
                                    .clickableWithNoRipple {
                                        onClickDeleteRecentSearch(index)
                                    },
                                painter = painterResource(Res.drawable.ic_cancel_300),
                                contentDescription = null,
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .background(Colors.Gray50),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    state = scrollState
                ) {
                    items(reviewList){
                        FeedView(
                            item = it,
                            searchKeyWord = query,
                            onClickMenu = {
                                clickedReviewId = it
                                scope.launch { sheetState.show() }
                            },
                            onClickLike = onClickLike,
                            onClickDetail = onClickDetail,
                        )

                        PHorizontalDivider()
                    }
                }
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