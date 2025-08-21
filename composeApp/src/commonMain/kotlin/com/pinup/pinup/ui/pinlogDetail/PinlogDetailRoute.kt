package com.pinup.pinup.ui.pinlogDetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pinup.pinup.ui.bookmark.BookmarkScreen
import com.pinup.pinup.ui.bookmark.BookmarkViewModel
import dev.icerock.moko.geo.compose.BindLocationTrackerEffect
import dev.icerock.moko.geo.compose.LocationTrackerAccuracy
import dev.icerock.moko.geo.compose.LocationTrackerFactory
import dev.icerock.moko.geo.compose.rememberLocationTrackerFactory
import kotlinx.collections.immutable.toPersistentList
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun PinlogDetailRoute(
    onBackPressed: () -> Unit = {},
) {

    PinlogDetailScreen(
        onBackPressed = onBackPressed
    )
}
