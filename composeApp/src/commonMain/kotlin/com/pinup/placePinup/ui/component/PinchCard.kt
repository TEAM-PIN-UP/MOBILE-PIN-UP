package com.pinup.placePinup.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pinup.placePinup.domain.model.PinchListItem
import com.pinup.placePinup.extentions.clickableWithNoRipple
import com.pinup.placePinup.ui.theme.Colors
import com.pinup.placePinup.ui.theme.Typography
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.ic_right_arrow_300

@Composable
fun PinchCard(
    pinchListItem: PinchListItem,
    onClickItem: (Int) -> Unit = {},
) {
    Row(
        modifier = Modifier
            .border(
                width = 1.dp,
                shape = RoundedCornerShape(12.dp),
                color = Colors.Gray200
            )
            .padding(vertical = 24.dp, horizontal = 16.dp)
            .clickableWithNoRipple {
                onClickItem(pinchListItem.id)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                text = pinchListItem.title,
                style = Typography.B1.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = Colors.Gray800
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = pinchListItem.description,
                style = Typography.B3.copy(
                    fontWeight = FontWeight.Normal
                ),
                color = Colors.Gray500
            )
        }

        Image(
            painter = painterResource(Res.drawable.ic_right_arrow_300),
            contentDescription = null
        )
    }
}