package com.pinup.pinup.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pinup.pinup.R
import com.pinup.pinup.extentions.clickableWithNoRipple
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.PinUPTheme
import com.pinup.pinup.ui.theme.Typography

@Composable
fun SearchedPlaceCard(
    name: String,
    address: String,
    reviewCount: Int,
    onClick: () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Colors.White)
            .clickableWithNoRipple {
                onClick()
            }
    ) {
        Image(
            painter = painterResource(Res.drawable.ic_category_food),
            contentDescription = "category icon",
        )

        Column(
            modifier = Modifier
                .padding(start = 12.dp)
        ) {
            Text(
                text = name,
                style = Typography.B3,
                color = Colors.Neutral800,
            )

            Text(
                text = address,
                style = Typography.C3,
                color = Colors.Neutral500,
            )
        }

        Spacer(Modifier.weight(1f))

        Text(
            text = "리뷰 $reviewCount",
            style = Typography.B5,
            color = Colors.Neutral600,
        )
    }
}

@Composable
@Preview
fun SearchedPlaceCardPreview() {
    PinUPTheme {
        SearchedPlaceCard(
            name = "잠실새내 딤딤섬",
            address = "서울 송파구 백제고분로7길 28-7 1층",
            reviewCount = 35
        )
    }
}