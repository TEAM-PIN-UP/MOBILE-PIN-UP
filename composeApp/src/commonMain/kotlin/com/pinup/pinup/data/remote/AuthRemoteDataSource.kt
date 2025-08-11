package com.pinup.pinup.data.remote

import com.pinup.pinup.data.request.EmailLoginRequest
import com.pinup.pinup.data.request.LoginRequest
import com.pinup.pinup.data.request.signUp.EmailSignUpRequest
import com.pinup.pinup.data.request.signUp.SocialSignUpRequest
import com.pinup.pinup.data.response.LoginResponse
import com.pinup.pinup.domain.model.PResult

interface AuthRemoteDataSource {
    suspend fun emailLogin(request: EmailLoginRequest): PResult<LoginResponse>
    suspend fun socialLogin(request: LoginRequest): PResult<LoginResponse>
    suspend fun logout(access: String): PResult<Unit>
    suspend fun socialSignUp(request: SocialSignUpRequest): PResult<Unit>
    suspend fun emailSignUp(request: EmailSignUpRequest): PResult<Unit>
}