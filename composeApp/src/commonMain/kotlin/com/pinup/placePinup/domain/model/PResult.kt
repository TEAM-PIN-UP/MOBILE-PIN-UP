package com.pinup.placePinup.domain.model

import com.pinup.placePinup.data.response.PResponse
import kotlinx.serialization.Serializable


@Serializable
sealed class PResult<out T> {
    data class Success<T>(val data: T): PResult<T>()
    data class Fail(val failState: FailState): PResult<Nothing>()
}

fun <T> PResult<T>.isSuccess(): Boolean {
    return when(this) {
        is PResult.Success -> true
        else -> false
    }
}

fun <T> PResult<T>.isFail(): Boolean {
    return when(this) {
        is PResult.Success -> false
        else -> true
    }
}

fun <T> PResult<T>.getSuccessOrNull(): T? {
    return when(this) {
        is PResult.Success -> this.data
        else -> null
    }
}

fun <T> PResult<T>.getOrElse(function: () -> T) : T {
    return when (this) {
        is PResult.Success -> this.data
        is PResult.Fail -> function()
    }
}

fun <S, T> PResult<T>.map(function: (T) -> S) : PResult<S> {
    return when (this) {
        is PResult.Success -> PResult.Success(function(this.data))
        is PResult.Fail -> this
    }
}

fun <T> PResult<PResponse<T>>.mapSuccessData() : PResult<T> {
    return when (this) {
        is PResult.Success -> PResult.Success(this.data.data ?: (Unit as T))
        is PResult.Fail -> this
    }
}

data class FailState(
    val status: Int,
    val code: String,
    val message: String
) {
    companion object {
        private const val EMPTY = ""
        val default = FailState(
            status = DEFAULT_ERROR,
            code = EMPTY,
            message = EMPTY
        )
    }
}

const val DEFAULT_ERROR = -1