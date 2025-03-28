package com.pinup.pinup.ui.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun <T> rememberPickerState(initialItem: T) = remember { PickerState(initialItem) }

class PickerState<T>(
    initialItem: T
) {
    var selectedItem by mutableStateOf(initialItem)
}

enum class DateType(val value: String) {
    YEAR("년"),
    MONTH("월"),
    DATE("일")
}