package com.pinup.pinup.ui.profilesetting

import androidx.compose.runtime.Composable
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileSettingRoute(
    onBackPressed: () -> Unit,
    viewModel: ProfileSettingViewModel = koinViewModel()
) {
    ProfileSettingScreen(
        onBackPressed = onBackPressed
    )
}