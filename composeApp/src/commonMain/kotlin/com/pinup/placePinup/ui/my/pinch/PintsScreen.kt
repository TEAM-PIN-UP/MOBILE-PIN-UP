package com.pinup.placePinup.ui.my.pinch
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pinup.placePinup.domain.model.PintsItem
import com.pinup.placePinup.extentions.ScrollToEndCallback
import com.pinup.placePinup.ui.component.PHorizontalDivider
import com.pinup.placePinup.ui.component.PinchItemView
import com.pinup.placePinup.ui.component.TitleBar
import com.pinup.placePinup.ui.theme.Colors
import com.pinup.placePinup.ui.theme.Texts
import com.pinup.placePinup.ui.theme.Typography

@Composable
fun PintsScreen(
    pintsList: List<PintsItem>,
    onMovePintsDetail: (Int) -> Unit = {},
    getMorePints: () -> Unit = {},
    onBackPressed: () -> Unit
) {
    val scrollState = rememberLazyListState()

    ScrollToEndCallback(scrollState) {
        getMorePints()
    }

    Column(
        modifier = Modifier
            .background(
                color = Colors.White
            )
            .statusBarsPadding()
            .fillMaxWidth()
    ) {
        TitleBar(
            modifier = Modifier
                .padding(horizontal = 20.dp),
            title = stringResource(Res.string.word_pinch),
            onLeftButtonClick = onBackPressed,
        )

        PHorizontalDivider()

        if (pintsList.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(pintsList) { it ->
                    PinchItemView(
                        pinchListItem = it,
                        onMoveDetail = onMovePintsDetail
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = Texts.PROFILE.EMPTY_USER_PINTS,
                    style = Typography.B1.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = Colors.Gray400,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
