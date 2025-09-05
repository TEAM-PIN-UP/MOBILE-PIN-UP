package com.pinup.pinup.ui.profilesetting

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileSettingRoute(
    onBackPressed: () -> Unit,
    viewModel: ProfileSettingViewModel = koinViewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    ProfileSettingScreen(
        onBackPressed = onBackPressed,
        onUpdateProfileImage = viewModel::updateProfile,
        profileImage = uiState.value.profileUrl ?: "",
        profileImageByte = uiState.value.profileByte,
        nickName = uiState.value.nickName,
        onNickNameChange = viewModel::updateNickName,
        bio = uiState.value.bio,
        onBioChange = viewModel::updateBio,
        onClickModifyProfile = viewModel::modifyProfile
    )
}