package com.pinup.pinup.data.impl

import com.pinup.pinup.data.local.MembersLocalDataSource
import com.pinup.pinup.data.remote.AuthRemoteDataSource
import com.pinup.pinup.data.request.LoginRequest
import com.pinup.pinup.data.request.SignUpRequest
import com.pinup.pinup.data.response.LoginResponse
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.SignUpInfo
import com.pinup.pinup.domain.model.TokenInfo
import com.pinup.pinup.domain.model.map
import com.pinup.pinup.domain.repository.AuthRepository
import com.pinup.pinup.extentions.toYnString
import javax.inject.Inject

class AuthRepositoryImpl (
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val membersLocalDataSource: MembersLocalDataSource,
): AuthRepository {
    override suspend fun login(socialId: String): PResult<LoginResponse> {
        return authRemoteDataSource.login(
            request = LoginRequest(
                socialId = socialId
            )
        )
    }

    override suspend fun logout(): PResult<Unit> {
        val accessToken = membersLocalDataSource.getAccessToken()
        return authRemoteDataSource.logout(accessToken)
    }

    override suspend fun signUp(signUpInfo: SignUpInfo): PResult<Unit> {
        return authRemoteDataSource.signUp(
            profileImage = signUpInfo.profileImage,
            request = SignUpRequest(
                name = signUpInfo.name,
                nickname = signUpInfo.nickname,
                email = signUpInfo.email,
                socialId = signUpInfo.socialId,
                loginType = signUpInfo.loginType,
                termsOfMarketing = signUpInfo.termsOfMarketing.toYnString()
            )
        )
    }
}