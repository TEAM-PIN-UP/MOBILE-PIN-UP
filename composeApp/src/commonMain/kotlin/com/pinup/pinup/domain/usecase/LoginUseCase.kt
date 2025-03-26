package com.pinup.pinup.domain.usecase

import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.TokenInfo
import com.pinup.pinup.domain.model.UserInfo
import com.pinup.pinup.domain.repository.AuthRepository
import com.pinup.pinup.ui.login.model.SNSUserInfo


class LoginUseCase (
    private val authRepository: AuthRepository,
    private val saveTokenUseCase: SaveTokenUseCase,
    private val saveUserInfoUseCase: SaveUserInfoUseCase,
) {
    suspend operator fun invoke(snsUserInfo: SNSUserInfo) : PResult<Unit> {
        return when (val result = authRepository.login(snsUserInfo.socialId)) {
            is PResult.Fail -> result
            is PResult.Success -> {
                saveUserInfoUseCase(
                    UserInfo(
                        memberId = result.data.memberResponse.memberId,
                        email = result.data.memberResponse.email,
                        name = result.data.memberResponse.name,
                        nickname = result.data.memberResponse.nickname,
                        profileUrl = result.data.memberResponse.profilePictureUrl ?: "",
                        snsType = snsUserInfo.snsType
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