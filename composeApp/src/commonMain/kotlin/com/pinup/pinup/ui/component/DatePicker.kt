package com.pinup.pinup.ui.component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import com.pinup.pinup.extentions.twoDigit
import com.pinup.pinup.ui.model.DateType
import com.pinup.pinup.ui.model.PickerState
import com.pinup.pinup.ui.model.rememberPickerState
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.util.DATE_RANGE
import com.pinup.pinup.util.MONTH_RANGE
import com.pinup.pinup.util.YEAR_RANGE
import com.pinup.pinup.util.currentDate
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.datetime.LocalDate
import kotlin.math.abs

@Composable
fun DatePicker(
    modifier: Modifier = Modifier,
    yearPickerState: PickerState<Int> = rememberPickerState(currentDate.year),
    monthPickerState: PickerState<Int> = rememberPickerState(currentDate.monthNumber),
    datePickerState: PickerState<Int> = rememberPickerState(currentDate.dayOfMonth),
    startLocalDate: LocalDate = currentDate,
    yearItems: List<Int> = YEAR_RANGE,
    monthItems: List<Int> = MONTH_RANGE,
    dateItems: List<Int> = DATE_RANGE,
    visibleItemsCount: Int = 5,
    itemPadding: PaddingValues = PaddingValues(8.dp),
    textStyle: TextStyle = TextStyle(fontSize = 16.sp, color = Colors.Neutral500),
    selectedTextStyle: TextStyle = TextStyle(fontSize = 22.sp, color = Colors.Neutral800),
    fadingEdgeGradient: Brush = Brush.verticalGradient(
        0f to Color.Transparent,
        0.5f to Color.Black,
        1f to Color.Transparent
    ),
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    spacingBetweenPickers: Dp = 20.dp,
    onCompleteClick: (String) -> Unit
) {

    Column {
        RoundedBox(
            modifier = modifier,
            cornerRounded = 12,
        ) {
            val yearStartIndex = remember {
                yearItems.indexOf(startLocalDate.year)
            }
            val monthStartIndex = remember {
                monthItems.indexOf(startLocalDate.monthNumber)
            }
            val dateStartIndex = remember {
                dateItems.indexOf(startLocalDate.dayOfMonth)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.spacedBy(
                    spacingBetweenPickers,
                    Alignment.CenterHorizontally
                ),
            ) {
                Picker(
                    state = yearPickerState,
                    dateType = DateType.YEAR,
                    modifier = Modifier.width(100.dp),
                    items = yearItems,
                    startIndex = yearStartIndex,
                    visibleItemsCount = visibleItemsCount,
                    textModifier = Modifier.padding(itemPadding),
                    textStyle = textStyle,
                    selectedTextStyle = selectedTextStyle,
                    itemPadding = itemPadding,
                    fadingEdgeGradient = fadingEdgeGradient,
                    horizontalAlignment = horizontalAlignment,
                    itemTextAlignment = verticalAlignment,
                )
                Picker(
                    state = monthPickerState,
                    dateType = DateType.MONTH,
                    items = monthItems,
                    startIndex = monthStartIndex,
                    visibleItemsCount = visibleItemsCount,
                    modifier = Modifier.width(70.dp),
                    textStyle = textStyle,
                    selectedTextStyle = selectedTextStyle,
                    textModifier = Modifier.padding(itemPadding),
                    itemPadding = itemPadding,
                    fadingEdgeGradient = fadingEdgeGradient,
                    horizontalAlignment = horizontalAlignment,
                    itemTextAlignment = verticalAlignment,
                )

                Picker(
                    state = datePickerState,
                    dateType = DateType.DATE,
                    items = dateItems,
                    startIndex = dateStartIndex,
                    visibleItemsCount = visibleItemsCount,
                    modifier = Modifier.width(70.dp),
                    textStyle = textStyle,
                    selectedTextStyle = selectedTextStyle,
                    textModifier = Modifier.padding(itemPadding),
                    itemPadding = itemPadding,
                    fadingEdgeGradient = fadingEdgeGradient,
                    horizontalAlignment = horizontalAlignment,
                    itemTextAlignment = verticalAlignment,
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .background(
                        color = Colors.Neutral200_20,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .align(Alignment.Center)
                    .height(40.dp)
            )
        }

        PButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            text = "선택 완료",
            onClick = {
                val yyyyMMdd = yearPickerState.selectedItem.toString().substring(2, 4) + "." + monthPickerState.selectedItem.twoDigit() +
                        "." + datePickerState.selectedItem.twoDigit()
                onCompleteClick(yyyyMMdd)
            }
        )
    }
}


@Composable
fun <T> Picker(
    items: List<T>,
    dateType: DateType,
    modifier: Modifier = Modifier,
    state: PickerState<T>,
    startIndex: Int = 0,
    visibleItemsCount: Int = 3,
    textModifier: Modifier = Modifier,
    textStyle: TextStyle = LocalTextStyle.current,
    selectedTextStyle: TextStyle = LocalTextStyle.current,
    itemPadding: PaddingValues = PaddingValues(8.dp),
    fadingEdgeGradient: Brush = Brush.verticalGradient(
        0f to Color.Transparent,
        0.5f to Color.Black,
        1f to Color.Transparent
    ),
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    itemTextAlignment: Alignment.Vertical = Alignment.CenterVertically,
    isInfinity: Boolean = true,
) {
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current
    val visibleItemsMiddle = remember { visibleItemsCount / 2 }

    val adjustedItems = if (!isInfinity) {
        listOf(null) + items + listOf(null)
    } else {
        items
    }

    val listScrollCount = if (isInfinity) {
        Int.MAX_VALUE
    } else {
        adjustedItems.size
    }

    val listScrollMiddle = remember { listScrollCount / 2 }
    val listStartIndex = remember {
        if (isInfinity) {
            listScrollMiddle - listScrollMiddle % adjustedItems.size - visibleItemsMiddle + startIndex
        } else {
            startIndex + 1
        }
    }

    fun getItem(index: Int) = adjustedItems[index % adjustedItems.size]

    val listState = rememberLazyListState(initialFirstVisibleItemIndex = listStartIndex)
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    val itemHeight = with(density) {
        selectedTextStyle.fontSize.toDp() + itemPadding.calculateTopPadding() + itemPadding.calculateBottomPadding() + 4.dp
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .mapNotNull { index -> getItem(index + visibleItemsMiddle) }
            .distinctUntilChanged()
            .collect { item -> state.selectedItem = item }
    }

    Box(modifier = modifier) {
        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            horizontalAlignment = horizontalAlignment,
            modifier = Modifier
                .align(Alignment.Center)
                .wrapContentSize()
                .height(itemHeight * visibleItemsCount)
                .fadingEdge(fadingEdgeGradient)
        ) {
            items(
                listScrollCount,
                key = { it },
            ) { index ->
                val fraction by remember {
                    derivedStateOf {
                        val currentItem =
                            listState.layoutInfo.visibleItemsInfo.firstOrNull { it.key == index }
                        currentItem?.offset?.let { offset ->
                            val itemHeightPx = with(density) { itemHeight.toPx() }
                            val fraction =
                                (offset - itemHeightPx * visibleItemsMiddle) / itemHeightPx
                            abs(fraction.coerceIn(-1f, 1f))
                        } ?: 0f
                    }
                }

                val currentItemText by remember {
                    mutableStateOf(if (getItem(index) == null) "" else getItem(index).toString())
                }

                Text(
                    text = currentItemText + dateType.value,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = textStyle.copy(
                        fontSize = lerp(
                            selectedTextStyle.fontSize,
                            textStyle.fontSize,
                            fraction
                        ),
                        color = lerp(
                            selectedTextStyle.color,
                            textStyle.color,
                            fraction
                        )
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .height(itemHeight)
                        .wrapContentHeight(align = itemTextAlignment)
                        .fillParentMaxWidth()
                        .then(textModifier)
                )
            }
        }
    }
}

private fun Modifier.fadingEdge(brush: Brush) = this
    .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
    .drawWithContent {
        drawContent()
        drawRect(brush = brush, blendMode = BlendMode.DstIn)
    }

@Composable
private fun pixelsToDp(pixels: Int) = with(LocalDensity.current) { pixels.toDp() }