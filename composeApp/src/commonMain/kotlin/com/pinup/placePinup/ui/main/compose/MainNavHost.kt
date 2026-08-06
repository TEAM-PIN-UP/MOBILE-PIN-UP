package com.pinup.placePinup.ui.main.compose

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.pinup.placePinup.domain.model.FCMType
import com.pinup.placePinup.event.DetailPlaceEventBus
import com.pinup.placePinup.platform.ContextFactory
import com.pinup.placePinup.platform.FcmBridgeStore
import com.pinup.placePinup.ui.article.ArticleRoute
import com.pinup.placePinup.ui.feed.FeedRoute
import com.pinup.placePinup.ui.main.MainViewModel
import com.pinup.placePinup.ui.map.MapRoute
import com.pinup.placePinup.ui.my.MyRoute
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
    onProfileModifyClick: () -> Unit = {},
    onMovePinBuddy: () -> Unit = {},
    onMoveScrap: () -> Unit = {},
    userId: Int = -1,
    onMovePintsDetail: (Int) -> Unit = {},
    updateUserId: (Int) -> Unit = {},
    onClickArticle: (Int) -> Unit = {},
    onMovePints: (Int) -> Unit = {},
    onMoveDetailImage: (Int, List<String>) -> Unit = {_,_ -> },
    onMoveNotification: () -> Unit = {},
) {
    val uiState = mainViewModel.uiState.collectAsStateWithLifecycle()
    val selectedMenuBar = remember { mutableStateOf<MainDestination>(MainDestination.Map) }
    val currentDestination = navHostController.currentBackStackEntryAsState().value?.destination
    val focusPlace by DetailPlaceEventBus.focusPlace.collectAsStateWithLifecycle()

    // 지도 밖에서 장소 센터링 요청이 들어오면 어느 탭에 있든 지도 탭으로 전환한다.
    LaunchedEffect(focusPlace) {
        if (focusPlace == null) return@LaunchedEffect
        navHostController.navigate(MainDestination.Map) {
            launchSingleTop = true
            restoreState = true
            popUpTo(navHostController.graph.startDestinationId) {
                saveState = true
            }
        }
    }

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
        if (userId == uiState.value.myId) {
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

    LaunchedEffect(mainViewModel.isMyPage) {
        if (mainViewModel.isMyPage) {
            navHostController.navigate(MainDestination.My) {
                launchSingleTop = true
                restoreState = true
                popUpTo(navHostController.graph.startDestinationId) {
                    saveState = true
                }
            }
            mainViewModel.isMyPage = false
        } else {
            return@LaunchedEffect
        }
    }

    LaunchedEffect(Unit) {
        FcmBridgeStore.pending.collect {
            if (it == null) return@collect
            val (type, targetId) = it

            when (FCMType.of(type)) {
                FCMType.FRIEND_LOG_SAME_PLACE,
                FCMType.FRIEND_LOG_CREATED -> onMovePlaceDetail(targetId.toString())
                FCMType.MEMORY_REMINDER,
                FCMType.LOG_LIKE,
                FCMType.LOG_COMMENT -> onMovePinlogDetail(targetId)
                FCMType.FRIEND_REQUEST -> onMovePinBuddy()
                FCMType.FRIEND_REQUEST_ACCEPTED -> onMoveUserProfileWithId(targetId)
                FCMType.SUMMARY_WEEKLY,
                FCMType.SUMMARY_MONTHLY -> {
                    navHostController.navigate(MainDestination.My) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(navHostController.graph.startDestinationId) {
                            saveState = true
                        }
                    }
                }
                FCMType.FEATURE_UPDATE,
                FCMType.ANNIVERSARY,
                FCMType.WEEKLY_RECOMMENDATION, //TODO 아티클로 보내는듯
                FCMType.DAILY_LOG_REMINDER,
                FCMType.WEEKLY_LOG_REMINDER,
                FCMType.DORMANT_USER_REENGAGEMENT -> {}
            }
            FcmBridgeStore.consume()
        }
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
                    focusPlaceId = focusPlace,
                    onConsumeFocusPlace = DetailPlaceEventBus::consumeFocusPlace,
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
                    },
                    onClickArticle = onClickArticle
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
                    onProfileModifyClick = {
                        onProfileModifyClick()
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
                    onMovePints = onMovePints,
                    onMovePlaceDetail = onMovePlaceDetail,
                    onMovePinchWrite = onMovePinchWrite,
                    onMovePintsDetail = onMovePintsDetail,
                    onMoveNotification = onMoveNotification
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