package com.pinup.pinup.ui.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pinup.pinup.domain.model.SortType
import com.pinup.pinup.extentions.clickableWithNoRipple
import com.pinup.pinup.ui.map.getNavigationBarHeight
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.PinUPTheme
import com.pinup.pinup.ui.theme.Typography
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortBottomSheet(
    selectedSortType: SortType,
    allPermissionsGranted: Boolean,
    onDismissRequest: () -> Unit,
    onSortTypeSelect: (SortType) -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    val navigationBarHeight = remember {
        derivedStateOf { getNavigationBarHeight(context) }
    }
    ModalBottomSheet(
        containerColor = Colors.White,
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
    ) {
        SortType.entries.forEach {
            if (allPermissionsGranted.not() && it == SortType.NEAR) return@forEach
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
                    .clickableWithNoRipple {
                        scope
                            .launch {
                                sheetState.hide()
                            }
                            .invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    onDismissRequest()
                                }
                            }
                        onSortTypeSelect(it)
                    },
                text = it.text,
                color = if (it == selectedSortType) Colors.Error else Colors.Neutral800,
                textAlign = TextAlign.Center,
                style = Typography.H3
            )
        }

        Spacer(modifier = Modifier.height(navigationBarHeight.value))
    }
}

@Composable
@Preview
private fun SortBottomSheetPreview() {
    PinUPTheme {
        SortBottomSheet(
            selectedSortType = SortType.NEAR,
            allPermissionsGranted = true,
            onDismissRequest = { },
            onSortTypeSelect = { },
        )
    }
}