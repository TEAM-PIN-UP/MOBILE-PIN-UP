package com.pinup.pinup.remote.api

import com.pinup.pinup.data.request.LoginRequest
import com.pinup.pinup.data.request.SignUpRequest
import com.pinup.pinup.data.response.LoginResponse
import com.pinup.pinup.data.response.PResponse
import com.pinup.pinup.domain.model.PResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.headersOf
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class AuthApi(
    private val httpClient: HttpClient
) {
    suspend fun refreshToken(token: String): PResult<PResponse<LoginResponse>> {
        val response = httpClient.post("api/auth/refresh") {
            headersOf("Refresh", token)
        }
        return response.body()
    }

    suspend fun login(request: LoginRequest): PResult<PResponse<LoginResponse>> {
        val response = httpClient.post("api/auth/login") {
            setBody(request)
        }
        return response.body()
    }

    suspend fun logout(token: String): PResult<PResponse<Unit>> {
        val response = httpClient.post("api/auth/logout") {
            headersOf("Access", token)
        }
        return response.body()
    }

    suspend fun signUp(byteArray: ByteArray, request: String): PResult<PResponse<Unit>> {
        val response = httpClient.post("api/auth/sign-up") {
            contentType(ContentType.MultiPart.FormData)
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append("multipartFile", byteArray)
                        append("signUpRequest", request)
                    }
                )
            )
        }
        return response.body()
    }
}