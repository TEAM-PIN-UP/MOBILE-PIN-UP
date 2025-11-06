package com.pinup.placePinup.domain.repository

import com.pinup.placePinup.data.request.EmailLoginRequest
import com.pinup.placePinup.data.response.LoginResponse
import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.model.SignUpInfo

interface AuthRepository {
    suspend fun emailLogin(request : EmailLoginRequest): PResult<LoginResponse>
    suspend fun socialLogin(provider : String, socialId: String): PResult<LoginResponse>
    suspend fun logout(): PResult<Unit>
    suspend fun socialSignUp(signUpInfo: SignUpInfo): PResult<LoginResponse>
    suspend fun emailSignUp(signUpInfo: SignUpInfo): PResult<LoginResponse>
}