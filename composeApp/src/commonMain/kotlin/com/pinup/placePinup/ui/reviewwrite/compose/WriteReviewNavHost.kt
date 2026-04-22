package com.pinup.placePinup.ui.reviewwrite.compose
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pinup.placePinup.ui.component.PDialog
import com.pinup.placePinup.ui.reviewwrite.WriteReviewUiEvent
import com.pinup.placePinup.ui.reviewwrite.WriteReviewViewModel
import com.pinup.placePinup.ui.reviewwrite.searchplace.SearchPlaceRoute
import com.pinup.placePinup.ui.theme.Colors
import com.pinup.placePinup.ui.theme.Texts
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun WriteReviewNavHost(
    writeReviewViewModel: WriteReviewViewModel = koinViewModel(),
    onBackPressed: () -> Unit,
    onCompleteAndBackPressed: () -> Unit = {},
    onMoveDetailPlace: (Int) -> Unit,
) {
    val uiState = writeReviewViewModel.uiState.collectAsStateWithLifecycle()
    val navHostController = rememberNavController()
    val isShowCompleteDialog = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        writeReviewViewModel.uiEvent.collect{
                when (it) {
                    WriteReviewUiEvent.MoveWriteReview -> {
                        navHostController.navigate(WriteReviewDestination.WriteReview)
                    }

                    WriteReviewUiEvent.SuccessWriteReview -> {
                        isShowCompleteDialog.value = true
                    }

                    WriteReviewUiEvent.SuccessEditReview -> onBackPressed()
                }
            }
    }

    LaunchedEffect(writeReviewViewModel.selectPlace){
        if(writeReviewViewModel.selectPlace != null) navHostController.navigate(WriteReviewDestination.SelectDate)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Colors.White
            )
    ) {
        NavHost(
            navController = navHostController,
            startDestination = if(writeReviewViewModel.reviewId == 0) WriteReviewDestination.SearchPlace else WriteReviewDestination.WriteReview,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None },
        ) {
            composable<WriteReviewDestination.SearchPlace> {
                SearchPlaceRoute(
                    onPlaceClick = {
                        writeReviewViewModel.selectPlace(it)
                        navHostController.navigate(WriteReviewDestination.SelectDate)
                    },
                    onBackPressed = onBackPressed,
                )
            }

            composable<WriteReviewDestination.SelectDate> {
                uiState.value.selectedPlace?.let {
                    SelectDateScreen(
                        placeName = it.name,
                        onSelectedDate = writeReviewViewModel::selectDate,
                        selectedDate = uiState.value.visitedDate,
                        onBackPressed = {
                            navHostController.popBackStack()
                        },
                        onClickNext = {
                            navHostController.navigate(WriteReviewDestination.WriteReview)
                        }
                    )
                }
            }

            composable<WriteReviewDestination.WriteReview> {
                uiState.value.selectedPlace?.let {
                    WriteReviewScreen(
                        placeName = it.name,
                        address = it.address,
                        reviewText = uiState.value.content,
                        rating = it.averageStarRating,
                        reviewCount = it.reviewCount,
                        imagePaths = uiState.value.imagePaths,
                        myRating = uiState.value.starRating,
                        isEnableButton = uiState.value.isEnableRegister,
                        clickedImage = uiState.value.clickedImage,
                        onValueChange = writeReviewViewModel::updateContent,
                        onRemoveImage = writeReviewViewModel::removeImage,
                        onAddImage = writeReviewViewModel::uploadImage,
                        onClickImage = writeReviewViewModel::onClickedImage,
                        onRatingSelected = writeReviewViewModel::updateRating,
                        onRegisterClick = writeReviewViewModel::uploadPinLog,
                        onBackPressed = {
                            if (writeReviewViewModel.reviewId != 0) onBackPressed() else navHostController.popBackStack()
                        }
                    )
                }
            }
        }
    }

    if (isShowCompleteDialog.value) {
        PDialog(
            titleText = Texts.PinLog.PINLOG_DIALOG_TITLE,
            descriptionText = Texts.PinLog.PINLOG_DIALOG_BODY,
            leftButtonText = stringResource(Res.string.word_do_return),
            rightButtonText = stringResource(Res.string.word_do_confirm),
            onLeftButtonClick = {
                isShowCompleteDialog.value = false
                onCompleteAndBackPressed()
            },
            onRightButtonClick = {
                isShowCompleteDialog.value = false
                writeReviewViewModel.writeReviewId?.let{
                    onMoveDetailPlace(it)
                }
            },
        )
    }
}

sealed interface WriteReviewDestination {
    @Serializable
    data object SearchPlace : WriteReviewDestination
    @Serializable
    data object SelectDate : WriteReviewDestination
    @Serializable
    data object WriteReview : WriteReviewDestination
}