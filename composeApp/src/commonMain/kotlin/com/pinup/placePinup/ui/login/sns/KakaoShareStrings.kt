package com.pinup.placePinup.ui.login.sns

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import org.jetbrains.compose.resources.stringResource
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.*

data class KakaoShareStrings(
    val title: String,
    val content: String,
    val button: String,
)

@Composable
fun rememberKakaoShareStrings(nickname: String): KakaoShareStrings {
    val title = stringResource(Res.string.kakao_profile_share_title, nickname)
    val content = stringResource(Res.string.kakao_profile_share_content, nickname)
    val button = stringResource(Res.string.kakao_profile_share_button)
    return remember(title, content, button) { KakaoShareStrings(title, content, button) }
}
