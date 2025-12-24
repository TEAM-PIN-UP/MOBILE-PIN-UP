package com.pinup.placePinup.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.pinup.placePinup.domain.model.Notification
import com.pinup.placePinup.ui.theme.Colors
import com.pinup.placePinup.ui.theme.Typography
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.ic_right_arrow

@Composable
fun NotificationView(
    notification: Notification
) {
    Row(
        modifier = Modifier
            .background(if (notification.isRead) Colors.White else Colors.Gray200)
            .fillMaxWidth()
            .height(75.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        //TODO 프로필 url로
        ProfileImageView(
            imgUrl = null,
            size = 36.dp
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            modifier = Modifier.weight(1f),
            text = notification.title,
            style = Typography.B3.copy(
                fontWeight = FontWeight.Medium
            ),
            color = Colors.Black,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.width(12.dp))

        // TODO 이미지가 비어있다면
        if (true) {
            Image(
                modifier = Modifier.size(16.dp),
                painter = painterResource(Res.drawable.ic_right_arrow),
                colorFilter = ColorFilter.tint(Colors.Gray400),
                contentDescription = null
            )
        } else {
            RoundedBox(
                cornerRounded = 8
            ) {
                AsyncImage(
                    modifier = Modifier
                        .size(50.dp),
                    //TODO 이미지 url로
                    model = notification.data,
                    contentScale = ContentScale.Crop,
                    contentDescription = null
                )
            }
        }

    }
}