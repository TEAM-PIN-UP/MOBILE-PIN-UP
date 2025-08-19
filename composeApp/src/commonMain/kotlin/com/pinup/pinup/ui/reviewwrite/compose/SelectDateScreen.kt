package com.pinup.pinup.ui.reviewwrite.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.pinup.pinup.extentions.clickableWithNoRipple
import com.pinup.pinup.ui.component.DatePicker
import com.pinup.pinup.ui.component.PButton
import com.pinup.pinup.ui.component.RoundedBox
import com.pinup.pinup.ui.component.TitleBar
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.Texts
import com.pinup.pinup.ui.theme.Typography
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.ic_calendar

@Composable
fun SelectDateScreen(
    placeName: String,
    onSelectedDate: (String) -> Unit = {},
    selectedDate: String = "",
    onBackPressed: () -> Unit = {},
    onClickNext: () -> Unit = {},
) {
    val isShowTimePicker = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Colors.White)
            .padding(horizontal = 20.dp)
    ) {
        TitleBar(
            title = Texts.PinLog.WRITE_PINLOG,
            onLeftButtonClick = onBackPressed
        )

        Spacer(modifier = Modifier.height(38.dp))

        Text(
            text = Texts.PinLog.getSelectDateTitle(placeName),
            style = Typography.D2.copy(
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        RoundedBox(
            modifier = Modifier
                .fillMaxWidth()
                .clickableWithNoRipple {
                    isShowTimePicker.value = true
                },
            cornerRounded = 100,
            cornerColor = Colors.Gray100
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .padding(start = 20.dp),
                    text = selectedDate.ifEmpty { Texts.PinLog.SELECT_DATE_HINT },
                    style = Typography.B2.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = if (selectedDate.isEmpty()) Colors.Gray600 else Colors.Gray800
                )

                Spacer(modifier = Modifier.weight(1f))

                Image(
                    painter = painterResource(Res.drawable.ic_calendar),
                    contentDescription = null
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        PButton(
            text = Texts.Word.NEXT,
            isEnable = selectedDate.isNotEmpty(),
            onClick = {
                onClickNext()
            }
        )
    }

    if (isShowTimePicker.value) {
        Dialog(
            onDismissRequest = {
                isShowTimePicker.value = false
            },
        ) {
            DatePicker(
                modifier = Modifier
                    .background(
                        color = Colors.White,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .wrapContentSize(),
                onCompleteClick = { visitedDate ->
                    isShowTimePicker.value = false
                    onSelectedDate(visitedDate)
                }
            )
        }
    }
}