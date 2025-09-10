package com.pinup.pinup.ui.main.compose

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import org.koin.compose.viewmodel.koinViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.pinup.pinup.PinUpAppDestination
import com.pinup.pinup.domain.model.DetailPlace
import com.pinup.pinup.domain.model.ReviewedPlace
import com.pinup.pinup.ui.bookmark.BookmarkRoute
import com.pinup.pinup.ui.component.BottomBar
import com.pinup.pinup.ui.feed.FeedRoute
import com.pinup.pinup.ui.main.MainViewModel
import com.pinup.pinup.ui.map.MapRoute
import com.pinup.pinup.ui.my.MyRoute
import kotlinx.serialization.Serializable

@Composable
fun MainNavHost(
    navHostController: NavHostController = rememberNavController(),
    mainViewModel: MainViewModel = koinViewModel(),
    onMoveWriteReview: (Int) -> Unit,
    onMoveNewWriteReview: (ReviewedPlace) -> Unit,
    onMovePinlogDetail: (Int) -> Unit,
    onMoveAddPinBuddy: () -> Unit,
    onMovePinBuddy: () -> Unit,
    onMoveSetting: () -> Unit,
    onClickEdit: (Int) -> Unit = {},
) {
    val uiState = mainViewModel.uiState.collectAsStateWithLifecycle()
    val selectedMenuBar = remember { mutableStateOf<MainDestination>(MainDestination.Map) }
    val currentDestination = navHostController.currentBackStackEntryAsState().value?.destination
    LaunchedEffect(currentDestination) {
        if (MainDestination.Map::class.qualifiedName == currentDestination?.route) {
            selectedMenuBar.value = MainDestination.Map
        } else if (MainDestination.Feed::class.qualifiedName == currentDestination?.route) {
            selectedMenuBar.value = MainDestination.Feed
        } else if (MainDestination.Article::class.qualifiedName == currentDestination?.route) {
            selectedMenuBar.value = MainDestination.Article
        } else if (MainDestination.My::class.qualifiedName == currentDestination?.route) {
            selectedMenuBar.value = MainDestination.My
        }
    }
    Column {
        NavHost(
            modifier = Modifier
                .weight(1f),
            startDestination = MainDestination.Map,
            navController = navHostController,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            composable<MainDestination.Map> {
                MapRoute(
                    onBottomMenuClick = {
                        if (MainDestination.Upload == it) {
                            onMoveWriteReview(0)
                        } else {
                            navHostController.navigate(it) {
                                launchSingleTop = true
                                restoreState = true
                                popUpTo(navHostController.graph.startDestinationId) {
                                    saveState = true
                                }
                            }
                        }
                    },
                    onClickEdit = {
                        onMoveWriteReview(it)
                    },
                    onMovePinlogDetail = onMovePinlogDetail,
                    onMoveWriteReview = onMoveNewWriteReview
                )
            }

            composable<MainDestination.Feed> {
                FeedRoute(
                    onClickBottomNav = {
                        if (MainDestination.Upload == it) {
                            onMoveWriteReview(0)
                        } else {
                            navHostController.navigate(it) {
                                launchSingleTop = true
                                restoreState = true
                                popUpTo(navHostController.graph.startDestinationId) {
                                    saveState = true
                                }
                            }
                        }
                    },
                    onClickEdit = {
                        onMoveWriteReview(it)
                    },
                    onClickDetail = {
                        onMovePinlogDetail(it)
                    }
                )
            }

            composable<MainDestination.Article> {

            }

            composable<MainDestination.My> {
                MyRoute(
                    onAddPinBuddyClick = onMoveAddPinBuddy,
                    onMovePinBuddy = onMovePinBuddy,
                    onMoveSetting = onMoveSetting,
                    onClickBottomNav = {
                        if (MainDestination.Upload == it) {
                            onMoveWriteReview(0)
                        } else {
                            navHostController.navigate(it) {
                                launchSingleTop = true
                                restoreState = true
                                popUpTo(navHostController.graph.startDestinationId) {
                                    saveState = true
                                }
                            }
                        }
                    },
                    onClickEdit = onClickEdit,
                    onClickPinLog = {
                        navHostController.navigate(MainDestination.Upload)
                    },
                    onClickDetail = onMovePinlogDetail
                )
            }
        }
    }
}

sealed interface MainDestination {
    @Serializable
    data object Map : MainDestination
    @Serializable
    data object Feed : MainDestination
    @Serializable
    data object Upload : MainDestination
    @Serializable
    data object Article : MainDestination
    @Serializable
    data object My : MainDestination
}