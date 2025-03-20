package com.pinup.pinup.domain.repository

import com.pinup.pinup.data.response.LoginResponse
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.SignUpInfo
import com.pinup.pinup.domain.model.TokenInfo

interface AuthRepository {
    suspend fun login(socialId: String): PResult<LoginResponse>
    suspend fun logout(): PResult<Unit>
    suspend fun signUp(signUpInfo: SignUpInfo): PResult<Unit>
}