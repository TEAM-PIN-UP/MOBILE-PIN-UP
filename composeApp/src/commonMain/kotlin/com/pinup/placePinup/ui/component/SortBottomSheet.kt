package com.pinup.placePinup.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pinup.placePinup.domain.model.SortType
import com.pinup.placePinup.extentions.clickableWithNoRipple
import com.pinup.placePinup.ui.theme.Colors
import com.pinup.placePinup.ui.theme.Typography
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.ic_check_main

@Composable
fun SortBottomSheet(
    selectedSortType: SortType,
    allPermissionsGranted: Boolean,
    onSortTypeSelect: (SortType) -> Unit,
) {
    Spacer(Modifier.height(40.dp))
    Column {
        SortType.entries.forEach {
            if (allPermissionsGranted.not() && it == SortType.NEAR) return@forEach
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clickableWithNoRipple {
                        onSortTypeSelect(it)
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (selectedSortType == it) {
                    Image(
                        painter = painterResource(Res.drawable.ic_check_main),
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.width(8.dp))
                }

                Text(
                    text = stringResource(it.textRes),
                    color = Colors.Gray800,
                    style = Typography.T2.copy(
                        fontWeight = FontWeight.Medium
                    )
                )
            }
            Spacer(Modifier.height(30.dp))
        }
    }

    Spacer(Modifier.height(34.dp))
}
