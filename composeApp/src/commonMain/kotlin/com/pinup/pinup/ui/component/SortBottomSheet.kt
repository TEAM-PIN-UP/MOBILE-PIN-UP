package com.pinup.pinup.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import coil3.compose.LocalPlatformContext
import com.pinup.pinup.domain.model.SortType

@Composable
fun SortBottomSheet(
    selectedSortType: SortType,
    allPermissionsGranted: Boolean,
    onDismissRequest: () -> Unit,
    onSortTypeSelect: (SortType) -> Unit,
) {
    val context = LocalPlatformContext.current
    val scope = rememberCoroutineScope()
//    val sheetState = rememberModalBottomSheetState()
    val navigationBarHeight = remember {
//        derivedStateOf { getNavigationBarHeight(context) }
    }
//    ModalBottomSheetLayout(
//        containerColor = Colors.White,
//        onDismissRequest = onDismissRequest,
//        sheetState = sheetState,
//    ) {
//        SortType.entries.forEach {
//            if (allPermissionsGranted.not() && it == SortType.NEAR) return@forEach
//            Text(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(vertical = 10.dp)
//                    .clickableWithNoRipple {
//                        scope
//                            .launch {
//                                sheetState.hide()
//                            }
//                            .invokeOnCompletion {
//                                if (!sheetState.isVisible) {
//                                    onDismissRequest()
//                                }
//                            }
//                        onSortTypeSelect(it)
//                    },
//                text = it.text,
//                color = if (it == selectedSortType) Colors.Error else Colors.Neutral800,
//                textAlign = TextAlign.Center,
//                style = Typography.H3
//            )
//        }
//
//        Spacer(modifier = Modifier.height(navigationBarHeight.value))
//    }
}
