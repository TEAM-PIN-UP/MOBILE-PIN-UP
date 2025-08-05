package com.pinup.pinup.remote.impl

import com.pinup.pinup.data.remote.AuthRemoteDataSource
import com.pinup.pinup.data.request.EmailLoginRequest
import com.pinup.pinup.data.request.LoginRequest
import com.pinup.pinup.data.request.SignUpRequest
import com.pinup.pinup.data.response.LoginResponse
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.mapSuccessData
import com.pinup.pinup.remote.api.AuthApi
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class AuthRemoteDataSourceImpl (
    private val authApi: AuthApi
): AuthRemoteDataSource {
    override suspend fun emailLogin(request: EmailLoginRequest): PResult<LoginResponse> {
        return authApi.socialLogin(request).mapSuccessData()
    }

    override suspend fun socialLogin(request: LoginRequest): PResult<LoginResponse> {
        return authApi.socialLogin(request).mapSuccessData()
    }

    override suspend fun logout(access: String): PResult<Unit> {
        return authApi.logout(access).mapSuccessData()
    }

    override suspend fun signUp(profileImage: ByteArray, request: SignUpRequest): PResult<Unit> {
        val timeStamp = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).nanosecond
        val signUpRequestString = Json.encodeToString(request)
        val multipart = MultiPartFormDataContent(formData {
            append("signUpRequest", signUpRequestString)
            append("multipartFile", profileImage, Headers.build {
                append(HttpHeaders.ContentType, "image/png")
                append(HttpHeaders.ContentDisposition, "filename=$timeStamp.png")
            })
        })
        return authApi.signUp(
            multipart = multipart
        ).mapSuccessData()
    }
}