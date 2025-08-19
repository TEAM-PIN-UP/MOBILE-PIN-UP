package com.pinup.pinup.remote.api

import com.pinup.pinup.data.request.EmailVerifyRequest
import com.pinup.pinup.data.request.SendVerifyCodeRequest
import com.pinup.pinup.data.response.PResponse
import com.pinup.pinup.domain.model.PResult
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.Multipart
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path
import io.ktor.client.request.forms.MultiPartFormDataContent

interface ImageApi {

    @Multipart
    @POST(ApiPath.ImagePath.SEVERAL_IMAGE)
    suspend fun uploadSeveralImages(
        @Path type : String,
        @Body multipart: MultiPartFormDataContent
    ): PResult<PResponse<Unit>>

    @Multipart
    @POST(ApiPath.ImagePath.ONE_IMAGE)
    suspend fun uploadImage(
        @Path type : String,
        @Body multipart: MultiPartFormDataContent
    ): PResult<PResponse<String>>
}