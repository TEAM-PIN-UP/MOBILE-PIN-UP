package com.pinup.pinup.ui.component

import android.os.SystemClock
import android.util.Log
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.pinup.pinup.extentions.toDp
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.PinUPTheme
import kotlin.math.abs

@Composable
fun PBottomSheet(
    expandedHeight: Dp,
    halfHeight: Dp,
    hiddenHeight: Dp,
    modifier: Modifier = Modifier,
    pBottomSheetTargetValue: PBottomSheetTargetValue = PBottomSheetTargetValue.HIDDEN,
    onSheetHeightChanged: (Dp) -> Unit = {},
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
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
                        velocityTracker.addPosition(SystemClock.uptimeMillis(), dragAmount)
                        if (expandedHeight >= realHeight - dragAmount.y.toDp(context).dp) {
                            realHeight -= dragAmount.y.toDp(context).dp
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
                .padding(top = 6.dp, bottom = 10.dp)
                .width(48.dp)
                .height(4.dp)
                .align(Alignment.CenterHorizontally)
                .background(
                    color = Colors.Neutral300,
                    shape = RoundedCornerShape(100.dp)
                )
        )

        content()
    }
}

enum class PBottomSheetTargetValue {
    EXPANDED, HALF, HIDDEN
}

@Preview
@Composable
private fun PBottomSheetPreview() {
    PinUPTheme {
        Column {
            PBottomSheet(
                content = {
                    Text("테스트")
                },
                expandedHeight = TODO(),
                halfHeight = TODO(),
                hiddenHeight = TODO(),
            )
        }
    }
}