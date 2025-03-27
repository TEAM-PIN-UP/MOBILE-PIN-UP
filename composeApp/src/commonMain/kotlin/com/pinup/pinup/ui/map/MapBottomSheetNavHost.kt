package com.pinup.pinup.ui.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pinup.pinup.domain.model.ReviewedPlace
import com.pinup.pinup.domain.model.SortType
import com.pinup.pinup.ui.model.ChipState
import kotlinx.collections.immutable.toPersistentList
import kotlinx.serialization.Serializable

@Composable
fun MapBottomSheetNavHost(
    searchUiState: SearchUiState,
    placeDetailUiState: PlaceDetailUiState,
    allPermissionsGranted: Boolean,
    isScrollable: Boolean,
    onValueChange: (String) -> Unit = {},
    onChipClick: (ChipState) -> Unit = {},
    onPlaceClick: (ReviewedPlace) -> Unit = {},
    onClearDetailPlace: () -> Unit = {},
    onUpdateBookmark: (String, Boolean) -> Unit = { _, _ -> },
    onUpdateSortType: (SortType) -> Unit,
    navHostController: NavHostController = rememberNavController()
) {
    LaunchedEffect(placeDetailUiState.detailPlace) {
        if (placeDetailUiState.detailPlace != null) {
            val currentDestination = navHostController.currentBackStackEntry?.destination?.route
            val detailDestination = MapBottomSheetDestination.Detail::class.qualifiedName
            if (currentDestination != detailDestination) {
                navHostController.navigate(MapBottomSheetDestination.Detail) {
                    launchSingleTop = true
                }
            }
        }
    }

    NavHost(
        navController = navHostController,
        startDestination = MapBottomSheetDestination.Search,
    ) {
        composable<MapBottomSheetDestination.Search> {
            MapBottomSheetSearchScreen(
                reviewedPlaces = searchUiState.reviewedPlaces.toPersistentList(),
                query = searchUiState.query,
                chipStates = searchUiState.chipStates.toPersistentList(),
                sortType = searchUiState.sortType,
                allPermissionsGranted = allPermissionsGranted,
                isExpanded = isScrollable,
                onValueChange = onValueChange,
                onChipClick = onChipClick,
                onPlaceClick = {
                    onPlaceClick(it)
                },
                onUpdateSortType = onUpdateSortType
            )
        }

        composable<MapBottomSheetDestination.Detail> {
            MapBottomSheetDetailScreen(
                detailPlace = placeDetailUiState.detailPlace,
                isExpanded = isScrollable,
                onBackPressed = {
                    onClearDetailPlace()
                    navHostController.popBackStack()
                },
                onClearDetailPlace = {
                    onClearDetailPlace()
                },
                onUpdateBookmark = { kakaoPlaceId, nowState ->
                    onUpdateBookmark(kakaoPlaceId, nowState)
                }
            )
        }
    }
}

sealed interface MapBottomSheetDestination {
    @Serializable
    data object Search: MapBottomSheetDestination
    @Serializable
    data object Detail: MapBottomSheetDestination
}