package com.pinup.pinup.ui.map

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pinup.pinup.domain.model.Category
import com.pinup.pinup.domain.model.DetailPlace
import com.pinup.pinup.domain.model.PinchListItem
import com.pinup.pinup.domain.model.ReviewedPlace
import com.pinup.pinup.platform.hLog
import com.pinup.pinup.ui.model.ChipState
import kotlinx.collections.immutable.toPersistentList
import kotlinx.serialization.Serializable

@Composable
fun MapBottomSheetNavHost(
    searchUiState: SearchUiState,
    placeDetailUiState: PlaceDetailUiState,
    pinchUiState: PinchUiState,
    clearPinchList: () -> Unit = {},
    isShowPinch: Boolean = false,
    isScrollable: Boolean,
    onValueChange: (String) -> Unit = {},
    onChipClick: (ChipState) -> Unit = {},
    onPlaceClick: (String) -> Unit = {},
    onPinchListClick: (Int) -> Unit = {},
    onClearDetailPlace: () -> Unit = {},
    onUpdateBookmark: (String, Boolean) -> Unit = { _, _ -> },
    onSelectSortTypeClick: () -> Unit = {},
    onFocusChange: (Boolean) -> Unit,
    navHostController: NavHostController = rememberNavController(),
    onClickMenu: (Int) -> Unit = {},
    onClickLike: (Int, Boolean) -> Unit = {_, _ -> },
    onMovePinlogDetail: (Int) -> Unit = {},
    onMoveWriteReview: (String) -> Unit = {},
) {
    val pinchRoute = MapBottomSheetDestination.Pinch::class.qualifiedName
    val detailRoute = MapBottomSheetDestination.Detail::class.qualifiedName

    LaunchedEffect(placeDetailUiState.detailPlace) {
        val current = navHostController.currentBackStackEntry?.destination?.route
        if (placeDetailUiState.detailPlace != null) {
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
            else {
                navHostController.popBackStack(
                    route = MapBottomSheetDestination.Search,
                    inclusive = false
                )
            }
        }
    }

    NavHost(
        navController = navHostController,
        startDestination = if (placeDetailUiState.detailPlace != null) MapBottomSheetDestination.Detail else MapBottomSheetDestination.Search,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None },
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
                onPlaceClick = onPlaceClick,
                onSelectSortTypeClick = onSelectSortTypeClick,
                onFocusChange = onFocusChange
            )
        }

        composable<MapBottomSheetDestination.Detail> {
            MapBottomSheetDetailScreen(
                detailPlace = placeDetailUiState.detailPlace,
                isExpanded = isScrollable,
                onMoveWriteReview = onMoveWriteReview,
                onBackPressed = {
                    onClearDetailPlace()
                    navHostController.popBackStack()
                },
                onClearDetailPlace = {
                    onClearDetailPlace()
                },
                onUpdateBookmark = { kakaoPlaceId, nowState ->
                    onUpdateBookmark(kakaoPlaceId, nowState)
                },
                onClickMenu = onClickMenu,
                onClickLike = onClickLike,
                onMovePinlogDetail = onMovePinlogDetail
            )
        }

        composable<MapBottomSheetDestination.Pinch> {
            MapBottomSheetPinchScreen(
                //TODO 여기도 임시, 테스트용 더미
                chipStates = listOf(
                    ChipState(
                        icon = null,
                        text = "더미1",
                        isSelected = true,
                        type = Category.NONE
                    ),
                    ChipState(
                        icon = null,
                        text = "더미2",
                        isSelected = false,
                        type = Category.NONE
                    ),
                    ChipState(
                        icon = null,
                        text = "더미3",
                        isSelected = false,
                        type = Category.NONE
                    ),
                ),
                pinchList = pinchUiState.pinchList,
                onClickPinch = { id ->
                    onPinchListClick(id)
                    navHostController.navigate(MapBottomSheetDestination.PinchDetail)
                }
            )
        }

        composable<MapBottomSheetDestination.PinchDetail> {
            MapBottomSheetPinchDetailScreen(
                id = 1,
                pinchUiState = pinchUiState,
                onClickBack = {
                    clearPinchList()
                    navHostController.popBackStack()
                },
                onClickPinch = { id ->
                    onPlaceClick(id)
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
    @Serializable
    data object Pinch: MapBottomSheetDestination
    @Serializable
    data object PinchDetail: MapBottomSheetDestination
}