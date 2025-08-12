package com.pinup.pinup.remote.impl

import com.pinup.pinup.data.remote.ImageRemoteDataSource
import com.pinup.pinup.domain.model.ImageUploadType
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.mapSuccessData
import com.pinup.pinup.remote.api.ImageApi
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class ImageRemoteDataSourceImpl (
    private val imageApi: ImageApi
): ImageRemoteDataSource {
    override suspend fun uploadSeveralImages(
        type: ImageUploadType,
        files: List<ByteArray>
    ): PResult<Unit> {
        val timeStamp = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).nanosecond
        val multipart = MultiPartFormDataContent(formData {
            files.forEach {
                append("multipartFiles", it, Headers.build {
                    append(HttpHeaders.ContentType, "image/png")
                    append(HttpHeaders.ContentDisposition, "filename=$timeStamp.png")
                })
            }
        })
        return imageApi.uploadSeveralImages(
            type = type.request,
            multipart = multipart
        ).mapSuccessData()
    }

    override suspend fun uploadImage(
        type: ImageUploadType,
        image: ByteArray
    ): PResult<Unit> {
        val timeStamp = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).nanosecond
        val multipart = MultiPartFormDataContent(formData {
            append("image", image, Headers.build {
                append(HttpHeaders.ContentType, "image/png")
                append(HttpHeaders.ContentDisposition, "filename=$timeStamp.png")
            })
        })
        return imageApi.uploadImage(
            type = type.request,
            multipart = multipart
        ).mapSuccessData()
    }
}