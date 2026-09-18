package com.pinup.placePinup.domain.usecase

import com.pinup.placePinup.domain.repository.AuthRepository
import com.pinup.placePinup.domain.repository.MembersRepository
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.authProvider
import io.ktor.client.plugins.auth.providers.BearerAuthProvider


class LogoutUseCase (
    private val authRepository: AuthRepository,
    private val membersRepository: MembersRepository,
    private val httpClient: HttpClient,
) {
    suspend operator fun invoke() {
        authRepository.logout()
        membersRepository.clearUserData()
        // Ktor Auth 플러그인은 토큰을 인메모리(AuthTokenHolder)로도 들고 있고,
        // 요청 시 defaultRequest가 붙인 Authorization을 지우고 이 캐시로 덮어쓴다.
        // DataStore만 비우면 HttpClient가 Koin single이라 프로세스 수명 내내 이전 계정
        // 토큰이 살아남아, 다른 계정으로 로그인해도 이전 계정 토큰이 전송된다.
        httpClient.authProvider<BearerAuthProvider>()?.clearToken()
    }
}
