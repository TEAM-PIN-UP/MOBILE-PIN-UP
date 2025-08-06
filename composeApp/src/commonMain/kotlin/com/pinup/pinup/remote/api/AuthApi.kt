package com.pinup.pinup.remote.api

import com.pinup.pinup.data.request.EmailLoginRequest
import com.pinup.pinup.data.request.LoginRequest
import com.pinup.pinup.data.response.LoginResponse
import com.pinup.pinup.data.response.PResponse
import com.pinup.pinup.domain.model.PResult
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.Header
import de.jensklingenberg.ktorfit.http.Multipart
import de.jensklingenberg.ktorfit.http.POST
import io.ktor.client.request.forms.MultiPartFormDataContent

interface AuthApi {
    @POST(ApiPath.Auth.REFRESH)
    suspend fun refreshToken(@Header("Refresh") token: String): PResult<PResponse<LoginResponse>>

    @POST(ApiPath.Auth.SOCIAL_LOGIN)
    suspend fun socialLogin(@Body request: LoginRequest): PResult<PResponse<LoginResponse>>

    @POST(ApiPath.Auth.EMAIL_LOGIN)
    suspend fun emailLogin(@Body request: EmailLoginRequest): PResult<PResponse<LoginResponse>>

    @POST(ApiPath.Auth.LOGOUT)
    suspend fun logout(@Header("Access") token: String): PResult<PResponse<Unit>>
}