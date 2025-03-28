package com.pinup.pinup.remote.impl

import com.pinup.pinup.data.remote.AuthRemoteDataSource
import com.pinup.pinup.data.request.LoginRequest
import com.pinup.pinup.data.request.SignUpRequest
import com.pinup.pinup.data.response.LoginResponse
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.mapSuccessData
import com.pinup.pinup.remote.api.AuthApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class AuthRemoteDataSourceImpl (
    private val authApi: AuthApi
): AuthRemoteDataSource {
    override suspend fun login(request: LoginRequest): PResult<LoginResponse> {
        return authApi.login(request).mapSuccessData()
    }

    override suspend fun logout(access: String): PResult<Unit> {
        return authApi.logout(access).mapSuccessData()
    }

    override suspend fun signUp(profileImage: ByteArray, request: SignUpRequest): PResult<Unit> {
        val requestBody = Json.encodeToString(request)
        return authApi.signUp(
            request = requestBody,
            byteArray = profileImage
        ).mapSuccessData()
    }
}