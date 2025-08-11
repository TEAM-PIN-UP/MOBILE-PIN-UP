package com.pinup.pinup.data.impl

import com.pinup.pinup.data.local.MembersLocalDataSource
import com.pinup.pinup.data.remote.AuthRemoteDataSource
import com.pinup.pinup.data.request.EmailLoginRequest
import com.pinup.pinup.data.request.LoginRequest
import com.pinup.pinup.data.request.signUp.EmailSignUpRequest
import com.pinup.pinup.data.request.signUp.SocialSignUpRequest
import com.pinup.pinup.data.response.LoginResponse
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.SignUpInfo
import com.pinup.pinup.domain.repository.AuthRepository
import com.pinup.pinup.extentions.toYnString

class AuthRepositoryImpl (
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val membersLocalDataSource: MembersLocalDataSource,
): AuthRepository {
    override suspend fun emailLogin(request: EmailLoginRequest): PResult<LoginResponse> {
        return authRemoteDataSource.emailLogin(request)
    }

    override suspend fun socialLogin(provider : String, socialId: String): PResult<LoginResponse> {
        return authRemoteDataSource.socialLogin(
            request = LoginRequest(
                provider = provider,
                socialId = socialId
            )
        )
    }

    override suspend fun logout(): PResult<Unit> {
        val accessToken = membersLocalDataSource.getAccessToken()
        return authRemoteDataSource.logout(accessToken)
    }

    override suspend fun socialSignUp(signUpInfo: SignUpInfo): PResult<Unit> {
        return authRemoteDataSource.socialSignUp(
            request = SocialSignUpRequest(
                name = signUpInfo.name,
                nickname = signUpInfo.nickname,
                email = signUpInfo.email,
                socialId = signUpInfo.socialId,
                loginType = signUpInfo.loginType,
                termsOfMarketing = signUpInfo.termsOfMarketing.toYnString()
            )
        )
    }

    override suspend fun emailSignUp(signUpInfo: SignUpInfo): PResult<Unit> {
        return authRemoteDataSource.emailSignUp(
            request = EmailSignUpRequest(
                email = signUpInfo.email,
                name = signUpInfo.name,
                password = signUpInfo.password,
                nickname = signUpInfo.nickname,
                loginType = signUpInfo.loginType,
                termsOfMarketing = signUpInfo.termsOfMarketing.toYnString()
            )
        )
    }
}