package com.pinup.placePinup.remote.api

import com.pinup.placePinup.data.response.PResponse
import com.pinup.placePinup.domain.model.PResult
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
    ): PResult<PResponse<List<String>>>

    @Multipart
    @POST(ApiPath.ImagePath.ONE_IMAGE)
    suspend fun uploadImage(
        @Path type : String,
        @Body multipart: MultiPartFormDataContent
    ): PResult<PResponse<String>>
}