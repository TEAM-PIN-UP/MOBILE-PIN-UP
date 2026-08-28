package com.pinup.placePinup.ui.map

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.pinup.placePinup.ui.model.ChipState
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
    onChipClick: (ChipState) -> Unit = {},
    onPintsChipClick: (ChipState) -> Unit = {},
    onPlaceClick: (String) -> Unit = {},
    onPinchListClick: (Int) -> Unit = {},
    onClearDetailPlace: () -> Unit = {},
    onUpdateBookmark: (String, Boolean) -> Unit = { _, _ -> },
    onSelectSortTypeClick: () -> Unit = {},
    navHostController: NavHostController = rememberNavController(),
    onClickMenu: (Int) -> Unit = {},
    onClickLike: (Int, Boolean) -> Unit = { _, _ -> },
    onMovePinlogDetail: (Int) -> Unit = {},
    onMoveWriteReview: (String) -> Unit = {},
    onMoveUserProfile: (String) -> Unit = {},
    onClickArticle: (Int) -> Unit = {},
) {
    val pinchRoute = MapBottomSheetDestination.Pinch::class.qualifiedName
    val detailRoute = MapBottomSheetDestination.Detail::class.qualifiedName
    val searchRoute = MapBottomSheetDestination.Search::class.qualifiedName

    val currentBackStackEntry by navHostController.currentBackStackEntryAsState()

    // 시스템(기기) 뒤로가기 등으로 시트 내비가 Detail -> Search 로 돌아오면 상세 상태도 함께 정리한다.
    // (시트 내 back 버튼은 onClearDetailPlace 를 직접 호출하지만, 시스템 back 은 내비만 pop 하고
    //  detailPlace 를 비우지 않아 지도의 선택 핀/하이라이트가 남던 문제 대응)
    // back stack 엔트리 변화에만 반응하므로 Detail 진입(마커 탭) 시점에는 트리거되지 않는다.
    LaunchedEffect(currentBackStackEntry) {
        val route = currentBackStackEntry?.destination?.route
        if (!isShowPinch && route == searchRoute && placeDetailUiState.detailPlace != null) {
            onClearDetailPlace()
        }
        // 방어: back 등으로 detailPlace 가 없는 Detail 엔트리에 도달하면 상세 화면이 빈(흰) 시트가 되므로 Search 로 되돌린다.
        if (route == detailRoute && placeDetailUiState.detailPlace == null) {
            navHostController.popBackStack(
                route = MapBottomSheetDestination.Search,
                inclusive = false
            )
        }
    }

    LaunchedEffect(placeDetailUiState.detailPlace) {
        val current = navHostController.currentBackStackEntry?.destination?.route
        if (placeDetailUiState.detailPlace != null) {
            if (current != detailRoute) {
                navHostController.navigate(MapBottomSheetDestination.Detail) {
                    launchSingleTop = true
                }
            }
        } else {
            if (isShowPinch) return@LaunchedEffect
            // 상세가 비워지면 Detail 엔트리를 스택에 남기지 않고 Search 로 pop 한다.
            // navigate() 로 Search 를 새로 쌓으면 [Search, Detail, Search] 가 되어, 이후 back 시
            // detailPlace=null 인 Detail 로 돌아가 빈(흰) 시트가 뜨던 문제를 방지.
            navHostController.popBackStack(
                route = MapBottomSheetDestination.Search,
                inclusive = false
            )
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
            navHostController.popBackStack(
                route = MapBottomSheetDestination.Search,
                inclusive = false
            )
        }
    }

    NavHost(
        navController = navHostController,
        startDestination = MapBottomSheetDestination.Search,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None },
    ) {
        composable<MapBottomSheetDestination.Search> {
            MapBottomSheetSearchScreen(
                reviewedPlaces = searchUiState.reviewedPlaces.toPersistentList(),
                chipStates = searchUiState.chipStates.toPersistentList(),
                sortType = searchUiState.sortType,
                isExpanded = isScrollable,
                onChipClick = onChipClick,
                onPlaceClick = onPlaceClick,
                onSelectSortTypeClick = onSelectSortTypeClick,
            )
        }

        composable<MapBottomSheetDestination.Detail> {
            MapBottomSheetDetailScreen(
                detailPlace = placeDetailUiState.detailPlace,
                isExpanded = isScrollable,
                onMoveWriteReview = onMoveWriteReview,
                onBackPressed = {
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
                onMovePinlogDetail = onMovePinlogDetail,
                onMoveUserProfile = onMoveUserProfile
            )
        }

        composable<MapBottomSheetDestination.Pinch> {
            MapBottomSheetPinchScreen(
                chipStates = pinchUiState.pintsCategoryList,
                pinchList = pinchUiState.pinchList,
                onClick = onPintsChipClick,
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
                },
                onClickArticle = onClickArticle
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