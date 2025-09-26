package com.pinup.pinup.ui.main.compose

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.pinup.pinup.platform.ContextFactory
import com.pinup.pinup.platform.hLog
import com.pinup.pinup.ui.article.ArticleRoute
import com.pinup.pinup.ui.bookmark.BookmarkRoute
import com.pinup.pinup.ui.component.BottomBar
import com.pinup.pinup.ui.component.NotDevelopScreen
import com.pinup.pinup.ui.feed.FeedRoute
import com.pinup.pinup.ui.main.MainViewModel
import com.pinup.pinup.ui.map.MapRoute
import com.pinup.pinup.ui.my.MyRoute
import com.pinup.pinup.ui.my.scrap.ScrapRoute
import kotlinx.serialization.Serializable

@Composable
fun MainNavHost(
    navHostController: NavHostController = rememberNavController(),
    mainViewModel: MainViewModel = koinViewModel(),
    contextFactory: ContextFactory,
    onMoveWriteReview: (Int) -> Unit,
    onMoveNewWriteReview: (String) -> Unit,
    onMovePinlogDetail: (Int) -> Unit,
    onMoveAddPinBuddy: () -> Unit,
    onMovePinBuddy: () -> Unit,
    onMoveUserProfile: (Int) -> Unit,
    onMoveSetting: () -> Unit,
    onClickEdit: (Int) -> Unit = {},
    onClickArticleDetail: (Int) -> Unit = {},
    onMovePlaceDetail: (String) -> Unit = {},
    userId: Int = -1
) {
    val uiState = mainViewModel.uiState.collectAsStateWithLifecycle()
    val selectedMenuBar = remember { mutableStateOf<MainDestination>(MainDestination.Map) }
    val userArgId = rememberSaveable { mutableIntStateOf(userId) }
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

    if (userArgId.intValue != -1) {
        LaunchedEffect(userArgId.intValue) {
            if (userArgId.intValue == uiState.value.myId) {
                navHostController.navigate(MainDestination.My) {
                    launchSingleTop = true
                    restoreState = true
                    popUpTo(navHostController.graph.startDestinationId) {
                        saveState = true
                    }
                }
            } else {
                onMoveUserProfile(userArgId.intValue)
            }
            userArgId.intValue = -1
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
                ArticleRoute(
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
                    onClickDetail = onClickArticleDetail
                )
            }

            composable<MainDestination.My> {
                MyRoute(
                    contextFactory = contextFactory,
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
                        onMoveWriteReview(0)
                    },
                    onClickDetail = onMovePinlogDetail,
                    onClickMoreScrap = {
                        navHostController.navigate(PinUpAppDestination.Scrap) {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo(navHostController.graph.startDestinationId) {
                                saveState = true
                            }
                        }
                    },
                    onMovePlaceDetail = onMovePlaceDetail
                )
            }

            composable<PinUpAppDestination.Scrap> {
                ScrapRoute(
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
                    onMovePlaceDetail = onMovePlaceDetail,
                    onSettingClick = onMoveSetting
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