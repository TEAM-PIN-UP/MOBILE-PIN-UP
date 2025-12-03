package com.pinup.placePinup.ui.image

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.toImmutableList
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DetailImageRoute(
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetailImageViewModel = koinViewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    DetailImageScreen(
        modifier = modifier,
        position = uiState.value.position ?: 0,
        images = uiState.value.images.toImmutableList(),
        onClose = onClose
    )
}
