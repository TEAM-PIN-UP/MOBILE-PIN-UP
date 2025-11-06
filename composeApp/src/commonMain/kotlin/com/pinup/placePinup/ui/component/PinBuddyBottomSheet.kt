package com.pinup.placePinup.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pinup.placePinup.extentions.clickableWithNoRipple
import com.pinup.placePinup.ui.theme.Colors
import com.pinup.placePinup.ui.theme.Texts
import com.pinup.placePinup.ui.theme.Typography

@Composable
fun PinBuddyBottomSheet(
    onClickRequestCancel: () -> Unit = {},
    onClickClose: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
    ) {
        Spacer(Modifier.height(40.dp))

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .clickableWithNoRipple {
                    onClickRequestCancel()
                },
            text = Texts.PROFILE.CANCEL_PIN_BUDDY_REQUEST,
            color = Colors.Negative,
            style = Typography.T2.copy(
                fontWeight = FontWeight.Medium
            )
        )

        Spacer(Modifier.height(30.dp))

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .clickableWithNoRipple {
                    onClickClose()
                },
            text = Texts.Word.CLOSE,
            color = Colors.Gray500,
            style = Typography.T2.copy(
                fontWeight = FontWeight.Medium
            )
        )

        Spacer(Modifier.height(64.dp))
    }
}
