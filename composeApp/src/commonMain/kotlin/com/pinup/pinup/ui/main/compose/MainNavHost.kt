package com.pinup.pinup.ui.main.compose

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.pinup.pinup.platform.ContextFactory
import com.pinup.pinup.ui.article.ArticleRoute
import com.pinup.pinup.ui.feed.FeedRoute
import com.pinup.pinup.ui.main.MainViewModel
import com.pinup.pinup.ui.map.MapRoute
import com.pinup.pinup.ui.my.MyRoute
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainNavHost(
    navHostController: NavHostController = rememberNavController(),
    mainViewModel: MainViewModel = koinViewModel(),
    contextFactory: ContextFactory,
    onMoveWriteReview: (Int) -> Unit,
    onMoveNewWriteReview: (String) -> Unit,
    onMovePinlogDetail: (Int) -> Unit,
    onMoveSetting: () -> Unit,
    onClickEdit: (Int) -> Unit = {},
    onClickArticleDetail: (Int) -> Unit = {},
    onMovePlaceDetail: (String) -> Unit = {},
    onMovePinchWrite: () -> Unit = {},
    onMoveUserProfileWithName: (String) -> Unit = {},
    onMoveUserProfileWithId: (Int) -> Unit = {},
    onMoveAddPinBuddy: () -> Unit = {},
    onMovePinBuddy: () -> Unit = {},
    onMoveScrap: () -> Unit = {},
    userId: Int = -1,
    onMovePintsDetail: (Int) -> Unit = {},
    updateUserId: (Int) -> Unit = {},
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

    LaunchedEffect(userId) {
        if (userId == -1) return@LaunchedEffect
        if (userId== uiState.value.myId) {
            navHostController.navigate(MainDestination.My) {
                launchSingleTop = true
                restoreState = true
                popUpTo(navHostController.graph.startDestinationId) {
                    saveState = true
                }
            }
        } else {
            onMoveUserProfileWithId(userId)
        }
        updateUserId(-1)
    }

    Column {
        NavHost(
            modifier = Modifier
                .weight(1f),
            startDestination = MainDestination.Map,
            navController = navHostController,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None },
        ) {
            composable<MainDestination.Map> {
                MapRoute(
                    onBottomMenuClick = {
                        if (it is MainDestination.Upload) {
                            if (it.isPinlogWrite) onMoveWriteReview(0) else onMovePinchWrite()
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
                    onMoveWriteReview = onMoveNewWriteReview,
                    onMoveUserProfile = {
                        onMoveUserProfileWithName(it)
                    }
                )
            }

            composable<MainDestination.Feed> {
                FeedRoute(
                    onClickBottomNav = {
                        if (it is MainDestination.Upload) {
                            if (it.isPinlogWrite) onMoveWriteReview(0) else onMovePinchWrite()
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
                    },
                    onMoveUserProfile = {
                        onMoveUserProfileWithName(it)
                    }
                )
            }

            composable<MainDestination.Article> {
                ArticleRoute(
                    onClickBottomNav = {
                        if (it is MainDestination.Upload) {
                            if (it.isPinlogWrite) onMoveWriteReview(0) else onMovePinchWrite()
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
                    onClickDetail = onClickArticleDetail
                )
            }

            composable<MainDestination.My> {
                MyRoute(
                    contextFactory = contextFactory,
                    onAddPinBuddyClick = {
                        onMoveAddPinBuddy()
                    },
                    onMovePinBuddy = {
                        onMovePinBuddy()
                    },
                    onMoveSetting = onMoveSetting,
                    onClickBottomNav = {
                        if (it is MainDestination.Upload) {
                            if (it.isPinlogWrite) onMoveWriteReview(0) else onMovePinchWrite()
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
                        onMoveWriteReview(0)
                    },
                    onClickDetail = onMovePinlogDetail,
                    onClickMoreScrap = {
                        onMoveScrap()
                    },
                    onMovePlaceDetail = onMovePlaceDetail,
                    onMovePinchWrite = onMovePinchWrite,
                    onMovePintsDetail = onMovePintsDetail
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
    data class Upload(
        val isPinlogWrite: Boolean = false
    ) : MainDestination
    @Serializable
    data object Article : MainDestination
    @Serializable
    data object My : MainDestination
}