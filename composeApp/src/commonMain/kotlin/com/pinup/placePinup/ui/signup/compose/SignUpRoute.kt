package com.pinup.placePinup.ui.signup.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.pinup.placePinup.ui.component.LoadingDialog
import com.pinup.placePinup.ui.login.model.SNSType
import com.pinup.placePinup.ui.signup.SignUpUiEvent
import com.pinup.placePinup.ui.signup.SignUpViewModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SignUpRoute(
    snsType: SNSType,
    onBackPressed: () -> Unit,
    onMoveMain: () -> Unit,
    navHostController: NavHostController = rememberNavController(),
    signUpViewModel: SignUpViewModel = koinViewModel()
) {
    val uiState = signUpViewModel.uiState.collectAsStateWithLifecycle()
    val isLoading = signUpViewModel.isLoading.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        signUpViewModel.uiEvent.collectLatest {
            when (it) {
                SignUpUiEvent.MoveMain -> onMoveMain()
            }
        }
    }

    SignUpScreen(
        navHostController = navHostController,
        snsType = snsType,
        emailState = uiState.value.emailState,
        passwordState = uiState.value.passwordState,
        nicknameState = uiState.value.nicknameState,
        profileUrl = uiState.value.profileUrl,
        termsOfServiceState = uiState.value.termsOfServiceState,
        onEmailChanged = signUpViewModel::updateEmail,
        onCodeChanged = signUpViewModel::updateVerificationCode,
        onPasswordChanged = signUpViewModel::updatePassword,
        onPasswordAgainChanged = signUpViewModel::updatePasswordAgain,
        onClickShowFirstPassword = signUpViewModel::onClickShowFirstPassword,
        onClickShowPassword = signUpViewModel::onClickShowPassword,
        onClickVerify = signUpViewModel::onClickVerify,
        onValueChange = signUpViewModel::updateNickName,
        onProfileImageChange = signUpViewModel::updateProfile,
        onTermAgreeClick = signUpViewModel::updateTermsOfServiceState,
        onSignUpClick = signUpViewModel::signUp,
        onBackPressed = onBackPressed
    )

    if (isLoading.value) {
        LoadingDialog()
    }
}
