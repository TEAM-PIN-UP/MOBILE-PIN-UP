package com.pinup.pinup.ui.reviewwrite.searchplace

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pinup.pinup.domain.model.Place
import kotlinx.collections.immutable.toPersistentList
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SearchPlaceRoute(
    onPlaceClick: (Place) -> Unit = {},
    onBackPressed: () -> Unit = {},
    searchPlaceViewModel: SearchPlaceViewModel = koinViewModel()
) {
    val uiState = searchPlaceViewModel.uiState.collectAsStateWithLifecycle()
    val query = searchPlaceViewModel.query.collectAsStateWithLifecycle()

    SearchPlaceScreen(
        query = query.value,
        places = uiState.value.places.toPersistentList(),
        onValueChange = searchPlaceViewModel::updateQuery,
        onPlaceClick = onPlaceClick,
        onBackPressed = onBackPressed
    )
}
