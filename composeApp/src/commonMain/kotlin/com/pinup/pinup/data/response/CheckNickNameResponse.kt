package com.pinup.pinup.data.response

import kotlinx.serialization.Serializable

@Serializable
data class CheckNickNameResponse(
    val exists: Boolean = false
) {
    companion object {
        fun CheckNickNameResponse.toModel(): Boolean {
            return exists
        }
    }
}
