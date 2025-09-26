package com.pinup.pinup.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.pinup.pinup.domain.model.Category
import com.pinup.pinup.domain.model.ReviewedPlace
import com.pinup.pinup.extentions.clickableWithNoRipple
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.Typography
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.ic_cafe
import pinup.composeapp.generated.resources.ic_food
import pinup.composeapp.generated.resources.ic_plus

@Composable
fun ScrapDetailItemView(
    place: ReviewedPlace,
    onMovePlaceDetail: (String) -> Unit = {},
) {
    val icon = when (place.placeCategory) {
        Category.RESTAURANT -> Res.drawable.ic_food
        Category.CAFE -> Res.drawable.ic_cafe
        else -> Res.drawable.ic_plus
    }
    Column {
        RoundedBox(
            modifier = Modifier
                .clickableWithNoRipple {
                    onMovePlaceDetail(place.kakaoPlaceId)
                },
            cornerRounded = 12
        ) {
            AsyncImage(
                modifier = Modifier
                    .aspectRatio(1f),
                model = place.reviewImageUrls[0],
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(icon),
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = place.name,
                color = Colors.Gray900,
                style = Typography.B2.copy(
                    fontWeight = FontWeight.SemiBold
                )
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = place.roadAddress,
            color = Colors.Gray400,
            style = Typography.L1.copy(
                fontWeight = FontWeight.Medium
            )
        )
    }
}