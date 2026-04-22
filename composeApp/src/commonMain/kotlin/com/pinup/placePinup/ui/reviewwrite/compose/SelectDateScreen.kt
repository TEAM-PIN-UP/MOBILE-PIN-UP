package com.pinup.placePinup.ui.reviewwrite.compose
import org.jetbrains.compose.resources.stringResource

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pinup.placePinup.extentions.clickableWithNoRipple
import com.pinup.placePinup.ui.component.PButton
import com.pinup.placePinup.ui.component.RoundedBox
import com.pinup.placePinup.ui.component.TitleBar
import com.pinup.placePinup.ui.theme.Colors
import com.pinup.placePinup.ui.theme.Texts
import com.pinup.placePinup.ui.theme.Typography
import com.pinup.placePinup.ui.component.datePicker.DatePicker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.*
import pinup.composeapp.generated.resources.ic_arrow_right_800
import pinup.composeapp.generated.resources.ic_back
import pinup.composeapp.generated.resources.ic_calendar

@Composable
fun SelectDateScreen(
    placeName: String,
    onSelectedDate: (String) -> Unit = {},
    selectedDate: String = "",
    onBackPressed: () -> Unit = {},
    onClickNext: () -> Unit = {},
) {
    val scope: CoroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true,
    )

    ModalBottomSheetLayout(
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetContent = {
            DatePickerScreen(
                onSelectedDate = {
                    onSelectedDate(it)
                    scope.launch { sheetState.hide() }
                }
            )
        },
        sheetBackgroundColor = Colors.White,
        sheetState = sheetState,
    ) {
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
                        scope.launch { sheetState.show() }
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

                    Spacer(modifier = Modifier.width(20.dp))
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            PButton(
                text = stringResource(Res.string.word_next),
                isEnable = selectedDate.isNotEmpty(),
                onClick = {
                    onClickNext()
                }
            )

            Spacer(modifier = Modifier.height(42.dp))
        }
    }
}
@Composable
fun DatePickerScreen(
    onSelectedDate: (String) -> Unit,
) {
    var selectedDate by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        DatePicker(
            modifier = Modifier
                .padding(vertical = 21.dp),
            titleStyle = Typography.T1.copy(
                fontWeight = FontWeight.SemiBold
            ),
            titleColor = Colors.Gray800,
            headStyle = Typography.T2.copy(
                fontWeight = FontWeight.Medium
            ),
            headColor = Colors.Gray800,
            dayStyle = Typography.B1.copy(
                fontWeight = FontWeight.Normal
            ),
            prevIcon = painterResource(Res.drawable.ic_back),
            nextIcon = painterResource(Res.drawable.ic_arrow_right_800),
            onSelectDate = {
                selectedDate = it.toString()
            }
        )

        Spacer(modifier = Modifier.height(22.dp))

        PButton(
            text = stringResource(Res.string.word_confirm),
            isEnable = true,
            onClick = {
                onSelectedDate(selectedDate)
            }
        )

        Spacer(modifier = Modifier.height(42.dp))
    }
}