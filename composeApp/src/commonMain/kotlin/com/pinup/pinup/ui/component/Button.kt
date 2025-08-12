package com.pinup.pinup.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pinup.pinup.extentions.clickableSingleWithNoRipple
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.Typography

@Composable
fun PButton(
    text: String,
    isEnable: Boolean = true,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isEnable) Colors.Gray800 else Colors.Gray300
    RoundedBox(
        modifier = modifier
            .fillMaxWidth()
            .clickableSingleWithNoRipple {
                if (isEnable) {
                    onClick()
                }
            },
        cornerRounded = 100,
        backgroundColor = backgroundColor,
    ) {
        Text(
            modifier = Modifier
                .padding(vertical = 15.dp)
                .align(Alignment.Center),
            text = text,
            style = Typography.T2.copy(
                fontWeight = FontWeight.Bold
            ),
            color = Colors.White
        )
    }
}