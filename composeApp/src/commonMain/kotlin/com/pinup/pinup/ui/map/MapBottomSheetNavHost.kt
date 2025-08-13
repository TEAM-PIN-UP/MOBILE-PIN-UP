package com.pinup.pinup.ui.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pinup.pinup.domain.model.ReviewedPlace
import com.pinup.pinup.ui.model.ChipState
import kotlinx.collections.immutable.toPersistentList
import kotlinx.serialization.Serializable

@Composable
fun MapBottomSheetNavHost(
    searchUiState: SearchUiState,
    placeDetailUiState: PlaceDetailUiState,
    isShowPinch: Boolean = false,
    isScrollable: Boolean,
    onValueChange: (String) -> Unit = {},
    onChipClick: (ChipState) -> Unit = {},
    onPlaceClick: (ReviewedPlace) -> Unit = {},
    onClearDetailPlace: () -> Unit = {},
    onUpdateBookmark: (String, Boolean) -> Unit = { _, _ -> },
    onSelectSortTypeClick: () -> Unit = {},
    onFocusChange: (Boolean) -> Unit,
    navHostController: NavHostController = rememberNavController()
) {
    val pinchRoute = MapBottomSheetDestination.Pinch::class.qualifiedName
    val detailRoute = MapBottomSheetDestination.Detail::class.qualifiedName

    LaunchedEffect(placeDetailUiState.detailPlace, isShowPinch) {
        if (!isShowPinch && placeDetailUiState.detailPlace != null) {
            val current = navHostController.currentBackStackEntry?.destination?.route
            if (current != detailRoute) {
                navHostController.navigate(MapBottomSheetDestination.Detail) {
                    launchSingleTop = true
                }
            }
        }
    }

    LaunchedEffect(isShowPinch) {
        val current = navHostController.currentBackStackEntry?.destination?.route
        if (isShowPinch) {
            if (current != pinchRoute) {
                navHostController.navigate(MapBottomSheetDestination.Pinch) {
                    launchSingleTop = true
                }
            }
        } else {
            if (current == pinchRoute) {
                navHostController.popBackStack()
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
                places = searchUiState.places,
                isExpanded = isScrollable,
                onValueChange = onValueChange,
                onChipClick = onChipClick,
                onPlaceClick = {
                    onPlaceClick(it)
                },
                onSelectSortTypeClick = onSelectSortTypeClick,
                onFocusChange = onFocusChange
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

        composable<MapBottomSheetDestination.Pinch> {

        }
    }
}

sealed interface MapBottomSheetDestination {
    @Serializable
    data object Search: MapBottomSheetDestination
    @Serializable
    data object Detail: MapBottomSheetDestination
    @Serializable
    data object Pinch: MapBottomSheetDestination
}