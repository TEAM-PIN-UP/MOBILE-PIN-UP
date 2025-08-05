package com.pinup.pinup.domain.usecase

import com.pinup.pinup.data.request.EmailLoginRequest
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.TokenInfo
import com.pinup.pinup.domain.model.UserInfo
import com.pinup.pinup.domain.repository.AuthRepository
import com.pinup.pinup.ui.login.model.SNSType
import com.pinup.pinup.ui.login.model.SNSUserInfo

class EmailLoginUseCase (
    private val authRepository: AuthRepository,
    private val saveTokenUseCase: SaveTokenUseCase,
    private val saveUserInfoUseCase: SaveUserInfoUseCase,
) {
    suspend operator fun invoke(request : EmailLoginRequest) : PResult<Unit> {
        return when (val result = authRepository.emailLogin(request)) {
            is PResult.Fail -> result
            is PResult.Success -> {
                saveUserInfoUseCase(
                    UserInfo(
                        memberId = result.data.memberResponse.memberId,
                        email = result.data.memberResponse.email,
                        name = result.data.memberResponse.name,
                        nickname = result.data.memberResponse.nickname,
                        profileUrl = result.data.memberResponse.profilePictureUrl ?: "",
                        snsType = SNSType.EMAIL
                    )
                )
                saveTokenUseCase(
                    TokenInfo(
                        accessToken = result.data.accessToken,
                        refreshToken = result.data.refreshToken
                    ))
            }
        }
    }
}