package com.pinup.pinup.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.Typography

@Composable
fun UserCard(
    imgUrl: String?,
    nickname: String,
    reviewCount: Int,
    pinBuddyCount: Int,
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

        Column(
            modifier = Modifier
                .padding(start = 8.dp)
        ) {
            Text(
                text = nickname,
                color = Colors.Neutral800,
                style = Typography.B3
            )

            Row(
                modifier = Modifier
                    .padding(top = 4.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "리뷰",
                        color = Colors.Neutral400,
                        style = Typography.B5
                    )
                    Text(
                        modifier = Modifier
                            .padding(start = 2.dp),
                        text = reviewCount.toString(),
                        color = Colors.Neutral800,
                        style = Typography.B5
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(start = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "핀버디",
                        color = Colors.Neutral400,
                        style = Typography.B5
                    )
                    Text(
                        modifier = Modifier
                            .padding(start = 2.dp),
                        text = pinBuddyCount.toString(),
                        color = Colors.Neutral800,
                        style = Typography.B5
                    )
                }
            }
        }

        Spacer(Modifier.weight(1f))

        buttonContainer()
    }
}