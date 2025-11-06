package com.pinup.placePinup.ui.component.datePicker
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pinup.placePinup.extentions.clickableWithNoRipple
import com.pinup.placePinup.ui.theme.Colors
import com.pinup.placePinup.ui.component.datePicker.DateTimeOperations.addMonth
import com.pinup.placePinup.ui.component.datePicker.DateTimeOperations.getLocalDate
import com.pinup.placePinup.ui.component.datePicker.DateTimeOperations.getCalendarDates
import com.pinup.placePinup.ui.component.datePicker.DateTimeOperations.getMonthName
import com.pinup.placePinup.ui.component.datePicker.DateTimeOperations.subtractMonth
import com.pinup.placePinup.ui.component.datePicker.DateTimeOperations.format
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate

@Composable
fun DatePicker(
    modifier: Modifier = Modifier,
    titleStyle: TextStyle = TextStyle(),
    titleColor: Color = Color.Black,
    headStyle: TextStyle = TextStyle(),
    headColor: Color = Color.Black,
    dayStyle: TextStyle = TextStyle(),
    prevIcon: Painter? = null,
    nextIcon: Painter? = null,
    range: Boolean = false,
    onSelectDate: (LocalDate) -> Unit = {},
    onRangeSelected: (LocalDate, LocalDate) -> Unit = { _, _ -> },
    clickable: Boolean = true,
    dateTimePickerDefaults: DateTimePickerDefaults = DateTimePickerDefaults(),
    dateTimePickerColors: DateTimePickerColors = DateTimePickerColors(
        selectedDateColor = Colors.Main,
        disabledDateColor = Colors.White,
        todayDateBorderColor = Colors.Main,
        rangeDateDateColor = Colors.White,
        textDisabledDateColor = Colors.Gray800,
        textSelectedDateColor = Colors.White,
        textTodayDateColor = Colors.Gray800,
        textCurrentMonthDateColor = Colors.Gray800,
        textOtherColor = Colors.Gray200,
    ),
) {
    DateTimeOperations.setDateTimePickerDefaults(dateTimePickerDefaults)

    var selectedMonth by remember { mutableStateOf(getLocalDate()) }

    var selectedDate by remember { mutableStateOf<CalendarDate?>(null) }
    var selectedSecondDate by remember { mutableStateOf<CalendarDate?>(null) }

    var formatedDayFrom by remember { mutableStateOf("") }
    var formatedDayTo by remember { mutableStateOf("") }
    var finalDate by remember { mutableStateOf<String?>(null) }

    val calendarDates by remember(selectedMonth, selectedDate, selectedSecondDate) {
        derivedStateOf {
            val dates = getCalendarDates(selectedMonth)
            if (selectedDate != null && selectedSecondDate != null && range) {
                val startDate = selectedDate!!
                val endDate = selectedSecondDate!!
                val start = if (startDate.date < endDate.date) startDate else endDate
                val end = if (startDate.date < endDate.date) endDate else startDate
                val rangeHasDisabledDate = dates.any { it.date in start.date..end.date && it.isDisabled }
                if (rangeHasDisabledDate) {
                    selectedDate = selectedSecondDate
                    selectedSecondDate = null
                    formatedDayFrom = selectedDate!!.date.format()
                    finalDate = formatedDayFrom
                    onSelectDate(selectedDate!!.date)
                    dates.map {
                        it.copy(isSelected = it.date == selectedDate!!.date)
                    }
                } else {
                    formatedDayFrom = start.date.format()
                    formatedDayTo = end.date.format()
                    finalDate = "$formatedDayFrom - $formatedDayTo"
                    onRangeSelected(start.date, end.date)
                    dates.map {
                        it.copy(
                            isInSelectedRange = it.date in start.date..end.date,
                            isSelected = it.date == start.date || it.date == end.date,
                            isStartOfRange = it.date == start.date
                        )
                    }
                }
            } else if (selectedDate != null) {
                formatedDayFrom = selectedDate!!.date.format()
                finalDate = formatedDayFrom
                if (!range) {
                    onSelectDate(selectedDate!!.date)
                }
                dates.map {
                    it.copy(isSelected = it.date == selectedDate!!.date)
                }
            } else {
                dates
            }
        }
    }

    Column(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = selectedMonth.getMonthName(),
                    style = titleStyle,
                    color = titleColor,
                    modifier = Modifier.weight(1f)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    prevIcon?.let {
                        Image(
                            painter = it,
                            contentDescription = "Previous month",
                            modifier = Modifier
                                .clickableWithNoRipple() {
                                    selectedMonth = selectedMonth.subtractMonth()
                                }
                        )
                    }

                    Spacer(modifier = Modifier.size(10.dp))

                    nextIcon?.let {
                        Image(
                            painter = it,
                            contentDescription = "Previous month",
                            modifier = Modifier
                                .clickableWithNoRipple {
                                    selectedMonth = selectedMonth.addMonth()
                                }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(50.dp))

            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                for (day in DayOfWeek.entries) {
                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = getKorean(day),
                            style = headStyle,
                            color = headColor,
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(color = Color(0xFFE5E5E5))
            )

            Spacer(modifier = Modifier.height(15.dp))

            for (i in calendarDates.indices step 7) {
                Row(
                    modifier = Modifier.padding(vertical = 1.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    for (j in i until i + 7) {
                        val calendarDate = calendarDates[j]
                        Box(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .weight(1f)
                                .datePickerBoxSelectedRange(calendarDate,dateTimePickerColors)
                                .clip(shape = RoundedCornerShape(100))
                                .datePickerBoxToday(calendarDate, dateTimePickerColors)
                                .datePickerBoxSelected(calendarDate, dateTimePickerColors)
                                .clickableWithNoRipple(enabled =
                                    !calendarDate.isDisabled && clickable && calendarDate.date <= getLocalDate()
                                ) {
                                    if (selectedDate == null) {
                                        selectedDate = calendarDate
                                    } else if (selectedSecondDate == null && range) {
                                        if (calendarDate.date > selectedDate!!.date) {
                                            selectedSecondDate = calendarDate
                                        } else {
                                            selectedDate = calendarDate
                                            selectedSecondDate = null
                                        }
                                    } else {
                                        selectedDate = calendarDate
                                        selectedSecondDate = null
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = calendarDate.day.toString(),
                                textAlign = TextAlign.Center,
                                maxLines = 1,
                                style = dayStyle,
                                color = when {
                                    calendarDate.isDisabled -> dateTimePickerColors.textDisabledDateColor
                                    calendarDate.isSelected -> dateTimePickerColors.textSelectedDateColor
                                    calendarDate.isToday -> dateTimePickerColors.textTodayDateColor
                                    calendarDate.isCurrentMonth -> dateTimePickerColors.textCurrentMonthDateColor
                                    else -> dateTimePickerColors.textOtherColor
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Modifier.datePickerBoxToday(date: CalendarDate, dateTimePickerColors: DateTimePickerColors): Modifier {
    return when (date.isToday) {
        true -> this.border(1.dp, dateTimePickerColors.todayDateBorderColor, shape = RoundedCornerShape(100))
        false -> this
    }
}

@Composable
private fun Modifier.datePickerBoxSelected(date: CalendarDate, dateTimePickerColors: DateTimePickerColors): Modifier {
    return when (date.isSelected) {
        true -> this.background(dateTimePickerColors.selectedDateColor, shape = RoundedCornerShape(100))
        false -> this
    }
}

@Composable
private fun Modifier.datePickerBoxDisabled(date: CalendarDate, dateTimePickerColors: DateTimePickerColors): Modifier {
    return when (date.isDisabled) {
        true -> this.background(dateTimePickerColors.disabledDateColor, shape = RoundedCornerShape(100))
        false -> this
    }
}

@Composable
private fun Modifier.datePickerBoxSelectedRange(date: CalendarDate, dateTimePickerColors: DateTimePickerColors): Modifier {
    return when (date.isInSelectedRange) {
        true -> {
            if (date.isSelected && date.isStartOfRange) {
                this.background(dateTimePickerColors.rangeDateDateColor, shape = RoundedCornerShape(topStart = 100.dp, bottomStart = 100.dp))
            } else if (date.isSelected) {
                this.background(dateTimePickerColors.rangeDateDateColor, shape = RoundedCornerShape(topEnd = 100.dp, bottomEnd = 100.dp))
            } else {
                this.background(dateTimePickerColors.rangeDateDateColor, shape = RoundedCornerShape(0.dp))
            }
        }
        false -> this
    }
}

private fun getKorean(day : DayOfWeek) : String {
    return when(day){
        DayOfWeek.MONDAY -> "월"
        DayOfWeek.TUESDAY -> "화"
        DayOfWeek.WEDNESDAY -> "수"
        DayOfWeek.THURSDAY -> "목"
        DayOfWeek.FRIDAY -> "금"
        DayOfWeek.SATURDAY -> "토"
        DayOfWeek.SUNDAY -> "일"
        else -> ""
    }
}