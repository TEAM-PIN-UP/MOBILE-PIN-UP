package com.pinup.placePinup.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pinup.placePinup.domain.model.Category
import com.pinup.placePinup.extentions.clickableWithNoRipple
import com.pinup.placePinup.ui.theme.Colors
import com.pinup.placePinup.ui.theme.Typography
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.*

@Composable
fun SearchedPlaceCard(
    name: String,
    category: Category,
    address: String,
    reviewCount: Int,
    onClick: () -> Unit = {},
) {
    val image = when (category) {
        Category.RESTAURANT -> Res.drawable.ic_category_food
        Category.CAFE -> Res.drawable.ic_category_cafe
        else -> Res.drawable.ic_category_place
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Colors.White)
            .clickableWithNoRipple {
                onClick()
            }
    ) {
        //TODO 카테고리 판별해서 알맞은 아이콘 보여주기
        Image(
            painter = painterResource(image),
            contentDescription = "category icon",
        )

        Spacer(modifier = Modifier.width(7.dp))

        Column(
            modifier = Modifier
        ) {
            Text(
                text = name,
                style = Typography.B2.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = Colors.Gray800,
            )

            Text(
                text = address,
                style = Typography.L1.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = Colors.Gray500,
            )
        }

        Spacer(Modifier.weight(1f))

        Text(
            text = "핀로그 $reviewCount",
            style = Typography.B2.copy(
                fontWeight = FontWeight.Medium
            ),
            color = Colors.Gray600,
        )
    }
}
