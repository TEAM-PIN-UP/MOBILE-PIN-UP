package com.pinup.placePinup.data.remote

import com.pinup.placePinup.data.request.EmailLoginRequest
import com.pinup.placePinup.data.request.LoginRequest
import com.pinup.placePinup.data.request.signUp.EmailSignUpRequest
import com.pinup.placePinup.data.request.signUp.SocialSignUpRequest
import com.pinup.placePinup.data.response.LoginResponse
import com.pinup.placePinup.domain.model.PResult

interface AuthRemoteDataSource {
    suspend fun emailLogin(request: EmailLoginRequest): PResult<LoginResponse>
    suspend fun socialLogin(request: LoginRequest): PResult<LoginResponse>
    suspend fun logout(access: String): PResult<Unit>
    suspend fun socialSignUp(request: SocialSignUpRequest): PResult<LoginResponse>
    suspend fun emailSignUp(request: EmailSignUpRequest): PResult<LoginResponse>
}