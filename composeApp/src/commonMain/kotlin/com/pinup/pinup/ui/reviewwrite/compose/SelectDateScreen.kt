package com.pinup.pinup.ui.reviewwrite.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.pinup.pinup.extentions.clickableWithNoRipple
import com.pinup.pinup.ui.component.DatePicker
import com.pinup.pinup.ui.component.RoundedBox
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.Typography
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.ic_calendar

@Composable
fun SelectDateScreen(
    placeName: String,
    onSelectedDate: (String) -> Unit = {}
) {
    val isShowTimePicker = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Colors.White)
            .padding(horizontal = 20.dp)

    ) {
        Image(
            modifier = Modifier
                .padding(top = 40.dp),
            painter = painterResource(Res.drawable.ic_calendar),
            contentDescription = null
        )

        Row(
            modifier = Modifier
                .padding(top = 16.dp),
        ) {
            Text(
                text = "‘$placeName’",
                color = Colors.Neutral800,
                style = Typography.H1
            )

            Text(
                modifier = Modifier
                    .padding(start = 4.dp),
                text = "은/는",
                color = Colors.Neutral400,
                style = Typography.H1
            )
        }

        Text(
            modifier = Modifier
                .padding(top = 8.dp),
            text = "언제 방문하셨나요?",
            color = Colors.Neutral400,
            style = Typography.H1
        )

        RoundedBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 28.dp)
                .clickableWithNoRipple {
                    isShowTimePicker.value = true
                },
            cornerRounded = 8,
            cornerColor = Colors.Neutral200
        ) {
            Text(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .padding(start = 12.dp),
                text = "날짜 선택",
                style = Typography.B3,
                color = Colors.Neutral800
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
}