package com.pinup.pinup.data.remote

import com.pinup.pinup.domain.model.PResult

interface ImageRemoteDataSource {
    suspend fun uploadSeveralImages(type: String, files: List<ByteArray>): PResult<Unit>
    suspend fun sendVerifyCode(type: String, image: ByteArray): PResult<Unit>
}