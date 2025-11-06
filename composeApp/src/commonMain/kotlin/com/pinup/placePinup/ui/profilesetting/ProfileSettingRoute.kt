package com.pinup.placePinup.ui.profilesetting

import PToastHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel
import rememberToastState

@Composable
fun ProfileSettingRoute(
    onBackPressed: () -> Unit,
    viewModel: ProfileSettingViewModel = koinViewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val toast = rememberToastState()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest {
            when (it) {
                ProfileSettingUiEvent.ErrorCantChangeName -> {
                    toast.show("")
                }
                ProfileSettingUiEvent.ErrorDuplicateName -> {
                    toast.show("")
                }
                ProfileSettingUiEvent.SuccessChangeProfile -> onBackPressed()
            }
        }
    }

    PToastHost(state = toast)

    ProfileSettingScreen(
        onBackPressed = onBackPressed,
        onUpdateProfileImage = viewModel::updateProfile,
        profileImage = uiState.value.profileUrl,
        profileImageByte = uiState.value.profileByte,
        nickName = uiState.value.nickName,
        onNickNameChange = viewModel::updateNickName,
        bio = uiState.value.bio,
        onBioChange = viewModel::updateBio,
        onClickModifyProfile = viewModel::onClickEditButton
    )
}