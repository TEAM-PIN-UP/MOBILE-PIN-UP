package com.pinup.placePinup.domain.usecase

import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.model.TokenInfo
import com.pinup.placePinup.domain.repository.MembersRepository
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.authProvider
import io.ktor.client.plugins.auth.providers.BearerAuthProvider


class SaveTokenUseCase (
    private val membersRepository: MembersRepository,
    private val httpClient: HttpClient,
) {
    suspend operator fun invoke(tokenInfo: TokenInfo) : PResult<Unit> {
        val result = membersRepository.saveToken(tokenInfo)
        // Auth 플러그인의 loadTokens 결과는 인메모리에 캐시된다. 로그인 전에 빈 토큰이
        // 캐시돼 있으면 새 토큰을 저장해도 요청은 계속 캐시로 나가므로, 저장 직후
        // 캐시를 비워 다음 요청에서 loadTokens가 새 토큰을 읽게 한다. (로그아웃과 대칭)
        httpClient.authProvider<BearerAuthProvider>()?.clearToken()
        return result
    }
}
