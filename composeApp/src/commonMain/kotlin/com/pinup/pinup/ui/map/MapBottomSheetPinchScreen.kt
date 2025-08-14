package com.pinup.pinup.ui.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pinup.pinup.domain.model.PinchListItem
import com.pinup.pinup.ui.component.Chips
import com.pinup.pinup.ui.component.PinchCard
import com.pinup.pinup.ui.model.ChipState
import com.pinup.pinup.ui.theme.Colors

@Composable
fun MapBottomSheetPinchScreen(
    chipStates: List<ChipState>,
    pinchList: List<PinchListItem>,
    onClick: (ChipState) -> Unit = {},
    onClickPinch: (Int) -> Unit = {},
) {
    val scrollState = rememberLazyListState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Colors.White
            )
    ) {
        Chips(
            modifier = Modifier
                .padding(start = 20.dp),
            chipStates = chipStates,
            onClick = onClick,
            isPinch = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .background(
                    color = Colors.White
                ),
            state = scrollState,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(pinchList) {
                PinchCard(
                    pinchListItem = it,
                    onClickItem = onClickPinch,
                )
            }
        }
    }
}