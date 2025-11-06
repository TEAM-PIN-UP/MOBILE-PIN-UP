package com.pinup.placePinup.domain.usecase

import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.model.SignUpInfo
import com.pinup.placePinup.domain.model.TokenInfo
import com.pinup.placePinup.domain.model.UserInfo
import com.pinup.placePinup.domain.repository.AuthRepository
import com.pinup.placePinup.ui.login.model.SNSType

class EmailSignUpUseCase (
    private val authRepository: AuthRepository,
    private val saveTokenUseCase: SaveTokenUseCase,
    private val saveUserInfoUseCase: SaveUserInfoUseCase,
) {
    suspend operator fun invoke(signUpInfo: SignUpInfo): PResult<Unit> {
        return when (val result = authRepository.emailSignUp(signUpInfo)) {
            is PResult.Fail -> result
            is PResult.Success -> {
                saveUserInfoUseCase(
                    UserInfo(
                        memberId = result.data.memberResponse.memberId,
                        email = result.data.memberResponse.email,
                        name = result.data.memberResponse.name,
                        nickname = result.data.memberResponse.nickname,
                        profileUrl = result.data.memberResponse.profilePictureUrl ?: "",
                        snsType = SNSType.PINUP
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