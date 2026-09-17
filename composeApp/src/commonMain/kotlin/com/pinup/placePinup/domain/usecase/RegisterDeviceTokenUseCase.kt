package com.pinup.placePinup.domain.usecase

import com.pinup.placePinup.data.request.fcm.SetDeviceTokenRequest
import com.pinup.placePinup.domain.model.FailState
import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.platform.FcmBridgeStore
import com.pinup.placePinup.platform.getPlatformName
import com.pinup.placePinup.platform.hLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * 로그인된 계정에 현재 기기의 FCM 토큰을 등록한다. 로그인·가입·자동 로그인 직후에 호출한다.
 *
 * 푸시 수신용 부가 기능이라 실패해도 로그인 흐름은 막지 않는다. 실패는 로그만 남기고,
 * 다음 앱 실행의 자동 로그인에서 다시 등록된다.
 */
class RegisterDeviceTokenUseCase(
    private val postSetDeviceTokenUseCase: PostSetDeviceTokenUseCase,
) {
    // 화면 전환과 무관하게 요청을 끝내기 위한 스코프. (UseCase 는 single 이라 앱 수명과 같다)
    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    suspend operator fun invoke(tokenTimeout: Duration = LOGIN_TOKEN_TIMEOUT): PResult<Unit> {
        val token = FcmBridgeStore.awaitFcmToken(tokenTimeout)
        val result = if (token == null) {
            PResult.Fail(FailState.default.copy(message = "FCM token not issued"))
        } else {
            postSetDeviceTokenUseCase(
                SetDeviceTokenRequest(
                    token = token,
                    platform = getPlatformName()
                )
            )
        }
        if (result is PResult.Fail) hLog("device token register failed: ${result.failState}")
        return result
    }

    /**
     * 기다리지 않고 등록만 시작한다. 스플래시에서 바로 메인으로 넘어가는 자동 로그인용.
     * (전환 애니메이션이 없어 viewModelScope 로 띄우면 화면이 넘어가는 즉시 요청이 취소된다)
     */
    fun launchInBackground() {
        appScope.launch { invoke(BACKGROUND_TOKEN_TIMEOUT) }
    }

    companion object {
        // 로그인 화면에 머무는 동안 토큰은 대부분 이미 발급돼 있어, 로딩을 오래 붙잡지 않도록 짧게 둔다.
        private val LOGIN_TOKEN_TIMEOUT = 3.seconds
        // 콜드 스타트 직후엔 토큰 발급이 늦을 수 있고, 화면을 막지 않으므로 넉넉히 기다린다.
        private val BACKGROUND_TOKEN_TIMEOUT = 30.seconds
    }
}
