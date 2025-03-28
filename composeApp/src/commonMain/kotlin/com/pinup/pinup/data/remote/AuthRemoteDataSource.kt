package com.pinup.pinup.data.remote

import com.pinup.pinup.data.request.LoginRequest
import com.pinup.pinup.data.request.SignUpRequest
import com.pinup.pinup.data.response.LoginResponse
import com.pinup.pinup.domain.model.PResult

interface AuthRemoteDataSource {
    suspend fun login(request: LoginRequest): PResult<LoginResponse>
    suspend fun logout(access: String): PResult<Unit>
    suspend fun signUp(profileImage: ByteArray, request: SignUpRequest): PResult<Unit>
}