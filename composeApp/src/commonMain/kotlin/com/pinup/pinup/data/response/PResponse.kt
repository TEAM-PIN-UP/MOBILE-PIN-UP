package com.pinup.pinup.data.response

import com.pinup.pinup.domain.model.FailState
import com.pinup.pinup.domain.model.PResult
import kotlinx.serialization.Serializable

@Serializable
data class PResponse<T>(
    val status: Int,
    val code: String,
    val message: String,
    val data: T? = null,
) {
    companion object {
        inline fun <reified T> PResponse<T>.toResult(): PResult<T> {
            if (status == 200) {
                this.data?.let {
                    return PResult.Success(it)
                } ?: run {
                    return PResult.Success(Unit as T)
                }
            } else {
                return PResult.Fail(
                    FailState(
                        status = this.status,
                        code = this.code,
                        message = this.message
                    )
                )
            }
        }
    }
}