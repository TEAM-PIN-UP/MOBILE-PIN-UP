package com.pinup.pinup.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pinup.pinup.extentions.clickableSingleWithNoRipple
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.PinUPTheme
import com.pinup.pinup.ui.theme.Typography

@Composable
fun PButton(
    text: String,
    isEnable: Boolean = true,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isEnable) Colors.Neutral800 else Colors.Neutral300
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
            style = Typography.H4,
            color = Colors.White
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PButtonEnablePreview() {
    PinUPTheme {
        PButton(
            text = "버튼",
            isEnable = true,
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PButtonPreview() {
    PinUPTheme {
        PButton(
            text = "버튼",
            isEnable = false,
            onClick = {}
        )
    }
}