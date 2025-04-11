package com.pinup.pinup.ui.profilesetting

import androidx.compose.runtime.Composable
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileSettingRoute(
    viewModel: ProfileSettingViewModel = koinViewModel()
) {
    ProfileSettingScreen()
}