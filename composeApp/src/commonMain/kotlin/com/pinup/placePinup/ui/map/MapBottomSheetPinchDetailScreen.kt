package com.pinup.placePinup.ui.map

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pinup.placePinup.extentions.clickableWithNoRipple
import com.pinup.placePinup.ui.component.ReviewedPlaceCard
import com.pinup.placePinup.ui.component.RoundedBox
import com.pinup.placePinup.ui.theme.Colors
import com.pinup.placePinup.ui.theme.Texts
import com.pinup.placePinup.ui.theme.Typography
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.ic_back

@Composable
fun MapBottomSheetPinchDetailScreen(
    id: Int,
    pinchUiState: PinchUiState,
    onClickArticle: (Int) -> Unit = {},
    onClickPinch: (String) -> Unit = {},
    onClickBack: () -> Unit = {},
) {
    val scrollState = rememberLazyListState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Colors.White
            )
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .padding(8.dp)
                    .clickableWithNoRipple {
                        onClickBack()
                    },
                painter = painterResource(Res.drawable.ic_back),
                contentDescription = "bottomsheet back"
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                modifier = Modifier
                    .weight(1f),
                text = pinchUiState.editorPintsDetail.title,
                color = Colors.Gray800,
                style = Typography.T1
            )

            Spacer(modifier = Modifier.width(8.dp))

            RoundedBox(
                modifier = Modifier
                    .clickableWithNoRipple {
                        onClickArticle(id)
                    },
                cornerRounded = 8,
                backgroundColor = Colors.Gray50,
            ) {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 10.dp),
                    text = Texts.PinMap.GO_ARTICLE,
                    color = Colors.Gray500,
                    style = Typography.L1
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier
                .background(color = Colors.Gray50),
            state = scrollState,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(pinchUiState.editorPintsDetail.pintsPlaceList) {
                ReviewedPlaceCard(
                    name = it.name,
                    rating = it.averageStarRating,
                    distance = it.distant,
                    reviewCount = it.reviewCount,
                    reviewerProfileImageUrls = it.reviewerProfileImages.map { profile ->
                        profile.url
                    },
                    reviewImageUrls = it.reviewImages.map { reviewImage ->
                        reviewImage.url
                    },
                    onItemClick = {
                        onClickPinch(it.kakaoPlaceId)
                    }
                )
            }
        }
    }
}