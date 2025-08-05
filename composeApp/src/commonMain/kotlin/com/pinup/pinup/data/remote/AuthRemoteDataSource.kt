package com.pinup.pinup.data.remote

import com.pinup.pinup.data.request.EmailLoginRequest
import com.pinup.pinup.data.request.LoginRequest
import com.pinup.pinup.data.request.SignUpRequest
import com.pinup.pinup.data.response.LoginResponse
import com.pinup.pinup.domain.model.PResult

interface AuthRemoteDataSource {
    suspend fun emailLogin(request: EmailLoginRequest): PResult<LoginResponse>
    suspend fun socialLogin(request: LoginRequest): PResult<LoginResponse>
    suspend fun logout(access: String): PResult<Unit>
    suspend fun signUp(profileImage: ByteArray, request: SignUpRequest): PResult<Unit>
}