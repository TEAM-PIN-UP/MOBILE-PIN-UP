package com.pinup.pinup.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.Typography

@Composable
fun UserCard(
    imgUrl: String?,
    nickname: String,
    modifier: Modifier = Modifier,
    buttonContainer: @Composable () -> Unit = {}
) {
    Row(
        modifier = modifier
            .background(Colors.White),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProfileImageView(
            imgUrl = imgUrl,
            size = 40.dp
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = nickname,
            color = Colors.Gray800,
            style = Typography.B1.copy(
                fontWeight = FontWeight.SemiBold
            )
        )

        Spacer(Modifier.weight(1f))

        buttonContainer()
    }
}