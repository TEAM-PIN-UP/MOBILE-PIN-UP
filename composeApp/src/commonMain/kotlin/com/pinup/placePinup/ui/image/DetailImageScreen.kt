package com.pinup.placePinup.ui.image

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.SubcomposeAsyncImage
import com.pinup.placePinup.ui.component.TitleBar
import com.pinup.placePinup.ui.model.TitleBarButtonType
import com.pinup.placePinup.ui.theme.Colors
import kotlinx.collections.immutable.ImmutableList
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable

@Composable
fun DetailImageScreen(
    position: Int,
    images: ImmutableList<String>,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val positionState = remember { mutableStateOf(position) }
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
            state = state
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
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
                        DetailAsyncImage(
                            imageUrl = images[it],
                            contentScale = contentScale
                        )
                    },
                )
            }
        }
    }
}

@Composable
private fun DetailAsyncImage(
    imageUrl: String,
    contentScale: ContentScale,
    modifier: Modifier = Modifier
) {
    val zoomState = rememberZoomState()

    AsyncImage(
        model = imageUrl,
        contentDescription = null,
        contentScale = contentScale,
        modifier = modifier
            .fillMaxSize()
            .zoomable(zoomState),
        onSuccess = { state ->
            zoomState.setContentSize(state.painter.intrinsicSize)
        }
    )
}
