package com.pinup.placePinup.ui.image

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculateCentroidSize
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastAny
import androidx.compose.ui.util.fastForEach
import coil3.compose.AsyncImage
import coil3.compose.SubcomposeAsyncImage
import com.pinup.placePinup.platform.hLog
import com.pinup.placePinup.ui.component.TitleBar
import com.pinup.placePinup.ui.model.TitleBarButtonType
import com.pinup.placePinup.ui.theme.Colors
import kotlinx.collections.immutable.ImmutableList
import kotlin.math.abs

@Composable
fun DetailImageScreen(
    position: Int,
    images: ImmutableList<String>,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val positionState = remember { mutableStateOf(position) }
    var zoom by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var initOffset by remember { mutableStateOf(Offset.Zero) }
    val userScrollEnabled by remember(zoom) {
        derivedStateOf {
            zoom == 1f
        }
    }
    val zoomAnim: Float by animateFloatAsState(
        targetValue = zoom,
        label = "zoomAnim"
    )

    val offsetAnim: Offset by animateOffsetAsState(
        targetValue = offset,
        label = "offsetAnim"
    )

    val state = rememberPagerState(
        initialPage = position
    ) { images.count() }

    LaunchedEffect(state.currentPage) {
        positionState.value = state.currentPage
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .navigationBarsPadding()
    ) {
        TitleBar(
            modifier = Modifier
                .padding(start = 20.dp),
            title = if (images.size == 1) "" else "${positionState.value + 1}/${images.count()}",
            buttonType = TitleBarButtonType.CLOSE,
            onLeftButtonClick = onClose
        )

        HorizontalPager(
            modifier = Modifier
                .fillMaxSize()
                .background(Colors.Black),
            state = state,
            userScrollEnabled = userScrollEnabled
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
//                    .pointerInput(Unit) {
//                        awaitEachGesture {
//                            val centerX = size.width / 2
//                            val centerY = size.height / 2
//                            var pastTouchSlop = false
//                            val touchSlop = viewConfiguration.touchSlop
//                            awaitFirstDown(requireUnconsumed = false, pass = PointerEventPass.Initial)
//                            do {
//                                val event = awaitPointerEvent(pass = PointerEventPass.Initial)
//                                val canceled = event.changes.fastAny { it.isConsumed }
//                                if (!canceled) {
//                                    val zoomChange = event.calculateZoom()
//                                    val panChange = event.calculatePan()
//
//                                    if (!pastTouchSlop) {
//                                        zoom *= zoomChange
//
//                                        val centroidSize = event.calculateCentroidSize(useCurrent = false)
//                                        val zoomMotion = abs(1 - zoom) * centroidSize
//
//                                        if (zoomMotion > touchSlop) {
//                                            pastTouchSlop = true
//                                        }
//                                    }
//                                    hLog("panChange >>> $panChange")
//                                    hLog("zoomChange >>> $zoomChange")
//                                    if (pastTouchSlop) {
//                                        if (zoomChange != 1f || panChange != Offset.Zero) {
//                                            val changedZoom = zoom * zoomChange
//                                            if (changedZoom >= 1f) zoom = changedZoom
//                                            offset += panChange
//                                            event.changes.fastForEach { it.consume() }
//                                        }
//                                    }
//                                }
//                            } while (!canceled && event.changes.fastAny { it.pressed })
//                            if (zoom < 1f) {
//                                zoom = 1f
//                                offset = initOffset
//                            }
//                        }
//                    }
            ) {
                SubcomposeAsyncImage(
                    model = images[it],
                    contentDescription = null,
                    loading = {
                        // 로딩 중
                        Box(modifier = Modifier.fillMaxSize())
                    },
                    success = { state ->
                        val imageSize = state.painter.intrinsicSize
                        val isFullWidth = imageSize.width > imageSize.height

                        val contentScale = if (isFullWidth) {
                            ContentScale.FillWidth
                        } else {
                            ContentScale.FillHeight
                        }

                        AsyncImage(
                            model = images[it],
                            contentDescription = null,
                            contentScale = contentScale,
                            modifier = Modifier
                                .fillMaxSize()
//                                .graphicsLayer {
//                                    val zoomedHeight = imageSize.height * zoom
//                                    hLog("zoomedHeight >>> $zoomedHeight")
//                                    scaleX = zoomAnim
//                                    scaleY = zoomAnim
//                                    translationX = offsetAnim.x
//                                    translationY = offsetAnim.y
//                                }
                        )
                    },
                )
            }

        }

    }
}