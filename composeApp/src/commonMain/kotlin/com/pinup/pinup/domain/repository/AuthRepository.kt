package com.pinup.pinup.domain.repository

import com.pinup.pinup.data.request.EmailLoginRequest
import com.pinup.pinup.data.response.LoginResponse
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.SignUpInfo

interface AuthRepository {
    suspend fun emailLogin(request : EmailLoginRequest): PResult<LoginResponse>
    suspend fun socialLogin(provider : String, socialId: String): PResult<LoginResponse>
    suspend fun logout(): PResult<Unit>
    suspend fun signUp(signUpInfo: SignUpInfo): PResult<Unit>
}