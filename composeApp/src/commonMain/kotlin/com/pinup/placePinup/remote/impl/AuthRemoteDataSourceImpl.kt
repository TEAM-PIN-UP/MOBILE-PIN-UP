package com.pinup.placePinup.remote.impl

import com.pinup.placePinup.data.remote.AuthRemoteDataSource
import com.pinup.placePinup.data.request.EmailLoginRequest
import com.pinup.placePinup.data.request.LoginRequest
import com.pinup.placePinup.data.request.signUp.EmailSignUpRequest
import com.pinup.placePinup.data.request.signUp.SocialSignUpRequest
import com.pinup.placePinup.data.response.LoginResponse
import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.model.mapSuccessData
import com.pinup.placePinup.remote.api.AuthApi
import com.pinup.placePinup.remote.api.MembersApi


class AuthRemoteDataSourceImpl (
    private val authApi: AuthApi,
    private val membersApi: MembersApi
): AuthRemoteDataSource {
    override suspend fun emailLogin(request: EmailLoginRequest): PResult<LoginResponse> {
        return authApi.emailLogin(request).mapSuccessData()
    }

    override suspend fun socialLogin(request: LoginRequest): PResult<LoginResponse> {
        return authApi.socialLogin(request).mapSuccessData()
    }

    override suspend fun logout(access: String): PResult<Unit> {
        return authApi.logout(access).mapSuccessData()
    }

    override suspend fun socialSignUp(request: SocialSignUpRequest): PResult<LoginResponse> {
        return membersApi.socialSignUp(request).mapSuccessData()
    }

    override suspend fun emailSignUp(request: EmailSignUpRequest): PResult<LoginResponse> {
        return membersApi.emailSignUp(request).mapSuccessData()
    }
}