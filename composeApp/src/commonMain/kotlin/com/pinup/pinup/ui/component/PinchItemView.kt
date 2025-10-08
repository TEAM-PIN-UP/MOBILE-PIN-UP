package com.pinup.pinup.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pinup.pinup.domain.model.PinchListItem
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.Typography
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.ic_share_root

@Composable
fun PinchItemView(
    pinchListItem: PinchListItem,
) {
    RoundedBox(
        modifier = Modifier
            .padding(horizontal = 20.dp),
        cornerColor = Colors.Gray200,
        cornerRounded = 12
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 23.dp, bottom = 22.dp),
            ) {
                Text(
                    text = pinchListItem.title,
                    color = Colors.Gray800,
                    style = Typography.T2.copy(
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = pinchListItem.description,
                    color = Colors.Gray500,
                    style = Typography.B3.copy(
                        fontWeight = FontWeight.Medium
                    )
                )
            }

            Image(
                painter = painterResource(Res.drawable.ic_share_root),
                contentDescription = null
            )
        }
    }
}