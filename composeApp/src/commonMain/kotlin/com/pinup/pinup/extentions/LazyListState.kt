package com.pinup.pinup.extentions

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember

fun LazyListState.isFirstItemVisible() = firstVisibleItemIndex == 0

@Composable
fun LazyListState.isScrollingDown(): Boolean {
    val offset by remember(this) { mutableIntStateOf(firstVisibleItemScrollOffset) }
    return remember(this) { derivedStateOf { (firstVisibleItemScrollOffset - offset) > 0 } }.value
}

@Composable
fun LazyListState.isScrollingUp(): Boolean {
    val offset by remember(this) { mutableIntStateOf(firstVisibleItemScrollOffset) }
    return remember(this) { derivedStateOf { (firstVisibleItemScrollOffset - offset) < 0 } }.value
}

@Composable
fun LazyListState.isAtBottom(): Boolean {

    return remember(this) {
        derivedStateOf {
            val visibleItemsInfo = layoutInfo.visibleItemsInfo
            if (layoutInfo.totalItemsCount == 0) {
                false
            } else {
                val lastVisibleItem = visibleItemsInfo.lastOrNull() ?: return@derivedStateOf false
                val viewportHeight = layoutInfo.viewportEndOffset + layoutInfo.viewportStartOffset

                (lastVisibleItem.index == layoutInfo.totalItemsCount - 1) &&
                        (lastVisibleItem.offset + lastVisibleItem.size <= viewportHeight)
            }
        }
    }.value
}

@Composable
fun ScrollToEndCallback(scrollState: LazyListState, callback: () -> Unit) {
    val isAtBottom = scrollState.isAtBottom()
    LaunchedEffect(isAtBottom) {
        if (isAtBottom) callback.invoke()
    }
}

fun <T> MutableList<T>.move(from: Int, to: Int) {
    if (from == to) return
    val element = this.removeAt(from)
    this.add(to, element)
}