package com.pinup.pinup.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.pinup.pinup.extentions.clickableSingleWithNoRipple
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.Typography

@Composable
fun PDialog(
    titleText: String,
    descriptionText: String = "",
    leftButtonText: String? = null,
    rightButtonText: String? = null,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
    onLeftButtonClick: () -> Unit = {},
    onRightButtonClick: () -> Unit = {},
    properties: DialogProperties = DialogProperties(
        usePlatformDefaultWidth = false
    ),
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = properties
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .clip(shape = RoundedCornerShape(16.dp))
                .background(color = Color.White)
                .padding(PaddingValues(all = 20.dp)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = titleText,
                color = Colors.Neutral800,
                style = Typography.H2,
                textAlign = TextAlign.Center
            )

            if (descriptionText.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = descriptionText,
                    color = Colors.Neutral500,
                    style = Typography.D1,
                    textAlign = TextAlign.Center,
                )
            }
            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                leftButtonText?.let {
                    Text(
                        text = it,
                        color = Colors.Neutral400,
                        style = Typography.H4,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .weight(1f)
                            .background(color = Colors.White)
                            .clickableSingleWithNoRipple { onLeftButtonClick.invoke() }
                            .padding(PaddingValues(vertical = 14.dp))
                    )
                }

                if (leftButtonText != null && rightButtonText != null) {
                    Spacer(modifier = Modifier.width(12.dp))
                }

                rightButtonText?.let {
                    Text(
                        text = rightButtonText,
                        color = Colors.White,
                        style = Typography.H4,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .weight(1f)
                            .clip(shape = RoundedCornerShape(100.dp))
                            .background(color = Colors.Neutral800)
                            .padding(PaddingValues(vertical = 14.dp))
                            .clickableSingleWithNoRipple { onRightButtonClick.invoke() }
                    )
                }
            }
        }
    }
}