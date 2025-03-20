package com.pinup.pinup.data.remote

import com.pinup.pinup.data.request.*
import com.pinup.pinup.data.response.*
import com.pinup.pinup.domain.model.PResult
import java.io.File

interface AuthRemoteDataSource {
    suspend fun login(request: LoginRequest): PResult<LoginResponse>
    suspend fun logout(access: String): PResult<Unit>
    suspend fun signUp(profileImage: File, request: SignUpRequest): PResult<Unit>
}