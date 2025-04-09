package com.pinup.pinup.ui.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pinup.pinup.domain.model.SortType
import com.pinup.pinup.extentions.clickableWithNoRipple
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.Typography

@Composable
fun SortBottomSheet(
    selectedSortType: SortType,
    allPermissionsGranted: Boolean,
    onSortTypeSelect: (SortType) -> Unit,
) {
    Spacer(Modifier.height(24.dp))
    SortType.entries.forEach {
        if (allPermissionsGranted.not() && it == SortType.NEAR) return@forEach
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
                .clickableWithNoRipple {
                    onSortTypeSelect(it)
                },
            text = it.text,
            color = if (it == selectedSortType) Colors.Error else Colors.Neutral800,
            textAlign = TextAlign.Center,
            style = Typography.H3
        )
    }
    Spacer(Modifier.height(24.dp))
}
