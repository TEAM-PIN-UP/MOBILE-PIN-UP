package com.pinup.pinup.remote.api

import com.pinup.pinup.data.request.LoginRequest
import com.pinup.pinup.data.request.SignUpRequest
import com.pinup.pinup.data.response.LoginResponse
import com.pinup.pinup.data.response.PResponse
import com.pinup.pinup.domain.model.PResult
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.Header
import de.jensklingenberg.ktorfit.http.Multipart
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Part
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.content.PartData
import io.ktor.http.contentType
import io.ktor.http.headersOf
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

interface AuthApi {
    @POST(AuthPath.REFRESH)
    suspend fun refreshToken(@Header("Refresh") token: String): PResult<PResponse<LoginResponse>>

    @POST(AuthPath.SOCIAL_LOGIN)
    suspend fun login(@Body request: LoginRequest): PResult<PResponse<LoginResponse>>

    @POST(AuthPath.LOGOUT)
    suspend fun logout(@Header("Access") token: String): PResult<PResponse<Unit>>

    //TODO memeber API로 이전
    @Multipart
    @POST("api/auth/sign-up")
    suspend fun signUp(
        @Body multipart: MultiPartFormDataContent
    ): PResult<PResponse<Unit>>
}