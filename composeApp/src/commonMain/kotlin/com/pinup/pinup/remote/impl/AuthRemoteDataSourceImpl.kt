package com.pinup.pinup.remote.impl

import com.pinup.pinup.data.remote.AuthRemoteDataSource
import com.pinup.pinup.data.request.LoginRequest
import com.pinup.pinup.data.request.SignUpRequest
import com.pinup.pinup.data.response.LoginResponse
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.mapSuccessData
import com.pinup.pinup.remote.api.AuthApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class AuthRemoteDataSourceImpl (
    private val authApi: AuthApi
): AuthRemoteDataSource {
    override suspend fun login(request: LoginRequest): PResult<LoginResponse> {
        return authApi.login(request).mapSuccessData()
    }

    override suspend fun logout(access: String): PResult<Unit> {
        return authApi.logout(access).mapSuccessData()
    }

    override suspend fun signUp(profileImage: File, request: SignUpRequest): PResult<Unit> {
        val requestBody = Json.encodeToString(request).toRequestBody("application/json".toMediaTypeOrNull())
        val multipartBody = MultipartBody.Part.createFormData("multipartFile", profileImage.name, profileImage.asRequestBody("image/jpeg".toMediaTypeOrNull()))
        return authApi.signUp(
            request = requestBody,
            file = multipartBody
        ).mapSuccessData()
    }
}