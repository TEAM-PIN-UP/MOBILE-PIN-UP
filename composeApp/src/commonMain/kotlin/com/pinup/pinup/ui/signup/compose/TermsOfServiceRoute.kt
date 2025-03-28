package com.pinup.pinup.ui.signup.compose

import androidx.compose.runtime.Composable
import com.pinup.pinup.ui.signup.TermsOfService
import com.pinup.pinup.ui.signup.TermsOfServiceState

@Composable
fun TermsOfServiceRoute(
    onAllAgreeClick: (TermsOfService) -> Unit,
    onUsingServiceAgreeClick: (TermsOfService) -> Unit,
    onCollectDataAgreeClick: (TermsOfService) -> Unit,
    onCollectLocationAgreeClick: (TermsOfService) -> Unit,
    onMarketingAgreeClick: (TermsOfService) -> Unit,
    onMoveComplete: () -> Unit,
    termsOfServiceState: TermsOfServiceState,
) {
    TermsOfServiceScreen(
        onAllAgreeClick = onAllAgreeClick,
        onUsingServiceAgreeClick = onUsingServiceAgreeClick,
        onCollectDataAgreeClick = onCollectDataAgreeClick,
        onCollectLocationAgreeClick = onCollectLocationAgreeClick,
        onMarketingAgreeClick = onMarketingAgreeClick,
        onMoveComplete = onMoveComplete,
        isAllAgree = termsOfServiceState.isAllAgree,
        isUsingServiceAgree = termsOfServiceState.isUsingServiceAgree,
        isCollectDataAgree = termsOfServiceState.isCollectDataAgree,
        isCollectLocationAgree = termsOfServiceState.isCollectLocationAgree,
        isMarketingAgreeClick = termsOfServiceState.isMarketingAgreeAgree,
        isPassValidation = termsOfServiceState.isPassValidation
    )
}