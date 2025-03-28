package com.pinup.pinup.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pinup.pinup.extentions.clickableWithNoRipple
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.Typography
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.*

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
