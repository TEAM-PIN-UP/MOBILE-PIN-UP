package com.pinup.placePinup.ui.component

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
//import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.pinup.placePinup.platform.pxToDp
import com.pinup.placePinup.ui.theme.Colors
import kotlinx.datetime.Clock
import kotlin.math.abs

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PBottomSheet(
    expandedHeight: Dp,
    halfHeight: Dp,
    hiddenHeight: Dp,
    modifier: Modifier = Modifier,
    pBottomSheetTargetValue: PBottomSheetTargetValue = PBottomSheetTargetValue.HIDDEN,
    onSheetHeightChanged: (Dp) -> Unit = {},
    consumeDetailClicked: () -> Unit = {},
    consumeMapClicked: () -> Unit = {},
    isMoving: Boolean = false,
    isDetailClicked: Boolean = false,
    isFocusSearch: Boolean = false,
    isShowPinch: Boolean = false,
    isMapClicked: Boolean = false,
    content: @Composable () -> Unit,
) {
    var currentTargetValue by remember { mutableStateOf(pBottomSheetTargetValue) }
    var realHeight by remember(expandedHeight, halfHeight, hiddenHeight) {
        mutableStateOf(
            when (currentTargetValue) {
                PBottomSheetTargetValue.EXPANDED -> expandedHeight
                PBottomSheetTargetValue.HALF -> halfHeight
                PBottomSheetTargetValue.HIDDEN -> hiddenHeight
            }
        )
    }
    val height by animateDpAsState(
        targetValue = realHeight,
        animationSpec = tween(durationMillis = 100, easing = LinearOutSlowInEasing),
        label = ""
    )
    val velocityTracker = remember { VelocityTracker() }
    var dragOffset by remember {
        mutableStateOf(Offset.Zero)
    }
    val isUpDrag by remember(dragOffset) {
        derivedStateOf {
            dragOffset.y < 0
        }
    }

    LaunchedEffect(isMoving){
        if (isMoving) {
            realHeight = if (isDetailClicked) halfHeight else hiddenHeight
            if(isDetailClicked) {
                consumeDetailClicked()
            }
        }
    }

    // 상세(detail)가 열리면 카메라 이동 타이밍과 무관하게 시트를 half로 올린다.
    // (피드 뱃지/검색으로 진입 시, 진입 시점에 카메라가 이미 이동 중이면 isMoving 이 새로 토글되지 않아
    //  위의 isMoving 이펙트만으로는 시트가 올라오지 않는 케이스 대응)
    LaunchedEffect(isDetailClicked) {
        if (isDetailClicked) {
            realHeight = halfHeight
        }
    }

    LaunchedEffect(isFocusSearch){
        if (isFocusSearch) realHeight = expandedHeight
    }

    LaunchedEffect(isShowPinch){
        if (isShowPinch) realHeight = halfHeight
    }

    LaunchedEffect(isMapClicked){
        if (isMapClicked) {
            realHeight = hiddenHeight
            consumeMapClicked()
        }
    }

//    BackHandler(
//        enabled = realHeight != hiddenHeight,
//        onBack = {
//            realHeight = hiddenHeight
//        }
//    )

    Column(
        modifier = modifier
            .height(height)
            .background(
                color = Colors.White,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            )
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {
                        velocityTracker.resetTracking()
                    },
                    onDragEnd = {
                        realHeight = if (abs(velocityTracker.calculateVelocity().y) > 300) {
                            velocityTracker.resetTracking()
                            if (isUpDrag) {
                                when (currentTargetValue) {
                                    PBottomSheetTargetValue.HALF,
                                    PBottomSheetTargetValue.EXPANDED -> {
                                        currentTargetValue = PBottomSheetTargetValue.EXPANDED
                                        expandedHeight
                                    }
                                    PBottomSheetTargetValue.HIDDEN -> {
                                        currentTargetValue = PBottomSheetTargetValue.HALF
                                        halfHeight
                                    }
                                }
                            } else {
                                when (currentTargetValue) {
                                    PBottomSheetTargetValue.EXPANDED -> {
                                        currentTargetValue = PBottomSheetTargetValue.HALF
                                        halfHeight
                                    }
                                    PBottomSheetTargetValue.HALF,
                                    PBottomSheetTargetValue.HIDDEN -> {
                                        currentTargetValue = PBottomSheetTargetValue.HIDDEN
                                        hiddenHeight
                                    }
                                }
                            }
                        } else {
                            if (realHeight >= expandedHeight) {
                                currentTargetValue = PBottomSheetTargetValue.EXPANDED
                                expandedHeight
                            } else if (realHeight in (halfHeight + expandedHeight) / 2 .. expandedHeight) {
                                currentTargetValue = PBottomSheetTargetValue.EXPANDED
                                expandedHeight
                            } else if (realHeight in halfHeight .. (halfHeight + expandedHeight) / 2) {
                                currentTargetValue = PBottomSheetTargetValue.HALF
                                halfHeight
                            } else {
                                currentTargetValue = PBottomSheetTargetValue.HIDDEN
                                hiddenHeight
                            }
                        }
                        onSheetHeightChanged(realHeight)
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        dragOffset = dragAmount
                        velocityTracker.addPosition(Clock.System.now().toEpochMilliseconds(), dragAmount)
                        if (expandedHeight >= realHeight - pxToDp(dragAmount.y).dp) {
                            realHeight -= pxToDp(dragAmount.y).dp
                        }
                        onSheetHeightChanged(realHeight)
                    },
                    onDragCancel = {
                        velocityTracker.resetTracking()
                    }
                )
            },
    ) {
        Box(
            modifier = Modifier
                .padding(top = 6.dp)
                .width(48.dp)
                .height(4.dp)
                .align(Alignment.CenterHorizontally)
                .background(
                    color = Colors.Neutral300,
                    shape = RoundedCornerShape(100.dp)
                )
        )

        Spacer(modifier = Modifier.height(18.dp))

        content()
    }
}

enum class PBottomSheetTargetValue {
    EXPANDED, HALF, HIDDEN
}
