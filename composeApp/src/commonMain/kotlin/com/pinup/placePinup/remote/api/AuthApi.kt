package com.pinup.placePinup.remote.api

import com.pinup.placePinup.data.request.EmailLoginRequest
import com.pinup.placePinup.data.request.LoginRequest
import com.pinup.placePinup.data.response.LoginResponse
import com.pinup.placePinup.data.response.PResponse
import com.pinup.placePinup.domain.model.PResult
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.Header
import de.jensklingenberg.ktorfit.http.POST

interface AuthApi {
    @POST(ApiPath.Auth.REFRESH)
    suspend fun refreshToken(@Header("Authorization") token: String): PResult<PResponse<LoginResponse>>

    @POST(ApiPath.Auth.SOCIAL_LOGIN)
    suspend fun socialLogin(@Body request: LoginRequest): PResult<PResponse<LoginResponse>>

    @POST(ApiPath.Auth.EMAIL_LOGIN)
    suspend fun emailLogin(@Body request: EmailLoginRequest): PResult<PResponse<LoginResponse>>

    @POST(ApiPath.Auth.LOGOUT)
    suspend fun logout(@Header("Access") token: String): PResult<PResponse<Unit>>
}