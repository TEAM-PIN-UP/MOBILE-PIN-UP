package com.pinup.placePinup.ui.notification
import org.jetbrains.compose.resources.stringResource

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.pinup.placePinup.domain.model.Notification
import com.pinup.placePinup.extentions.ScrollToEndCallback
import com.pinup.placePinup.extentions.clickableWithNoRipple
import com.pinup.placePinup.ui.component.NotificationView
import com.pinup.placePinup.ui.component.PHorizontalDivider
import com.pinup.placePinup.ui.theme.Colors
import com.pinup.placePinup.ui.theme.Texts
import com.pinup.placePinup.ui.theme.Typography
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.*
import pinup.composeapp.generated.resources.ic_back

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NotificationScreen(
    notificationList: List<Notification>,
    isRefreshing: Boolean,
    getMoreNotification: () -> Unit = {},
    onRefresh: () -> Unit = {},
    onClickBack: () -> Unit = {},
    onClickNotification: (Notification) -> Unit = {},
    onClickAllReadNotification: () -> Unit = {},
    unReadCount: Int = 0,
) {
    val scrollState = rememberLazyListState()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = onRefresh
    )

    ScrollToEndCallback(scrollState) {
        getMoreNotification()
    }

    Column(
        modifier = Modifier
            .background(
                color = Colors.White
            )
            .statusBarsPadding()
            .fillMaxSize()
    ) {
        Box {
            Row(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    modifier = Modifier
                        .clickableWithNoRipple { onClickBack() },
                    painter = painterResource(Res.drawable.ic_back),
                    contentDescription = null,
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    modifier = Modifier
                        .clickableWithNoRipple {
                            onClickAllReadNotification()
                        },
                    text = if (unReadCount == 0) Texts.Notification.ALL_READ else "안읽음 $unReadCount",
                    style = Typography.B2.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = Colors.Main
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(Res.string.word_notification),
                    style = Typography.B1.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = Colors.Gray800
                )
            }
        }

        PHorizontalDivider()

        Box(
            Modifier
                .weight(1f)
                .pullRefresh(
                    state = pullRefreshState,
                )
        ) {
            LazyColumn(
                modifier = Modifier
                    .background(Colors.White),
                state = scrollState,
            ) {
                items(notificationList) {
                    NotificationView(
                        notification = it,
                        onClickNotification = onClickNotification
                    )
                }

                item {
                    Spacer(
                        modifier = Modifier.navigationBarsPadding()
                    )
                }
            }

            PullRefreshIndicator(
                refreshing = isRefreshing,
                state = pullRefreshState,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .zIndex(1f)
            )
        }
    }
}