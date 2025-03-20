package com.pinup.pinup.remote.network

import android.util.Log
import com.pinup.pinup.data.response.PResponse
import com.pinup.pinup.domain.model.FailState
import com.pinup.pinup.domain.model.PResult
import kotlinx.serialization.json.Json
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NetworkResponseCall<T>(
    private val delegate: Call<T>,
) : Call<PResult<T>> {
    override fun enqueue(callback: Callback<PResult<T>>) {
        return delegate.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                try {
                    val body = response.body()
                    val error = response.errorBody()
                    if (response.isSuccessful) {
                        if (body != null) {
                            callback.onResponse(
                                this@NetworkResponseCall,
                                Response.success(PResult.Success(body))
                            )
                        }
                    } else {
                        val errorBody = when {
                            error == null -> null
                            error.contentLength() == 0L -> null
                            else -> try {
                                error
                            } catch (ex: Exception) {
                                null
                            }
                        }
                        if (errorBody != null) {
                            try {
                                val errorData: PResponse<Nothing> = Json.decodeFromString(errorBody.string())
                                callback.onResponse(
                                    this@NetworkResponseCall,
                                    Response.success(PResult.Fail(FailState(
                                        code = errorData.code,
                                        status = errorData.status,
                                        message = errorData.message
                                    )))
                                )
                            } catch (e: Exception) {
                                callback.onResponse(
                                    this@NetworkResponseCall,
                                    Response.success(PResult.Fail(FailState.default.copy(
                                        message = e.message ?: ""
                                    )))
                                )
                            }
                        } else {
                            callback.onResponse(
                                this@NetworkResponseCall,
                                Response.success(PResult.Fail(FailState.default))
                            )
                        }
                    }
                } catch (e: Exception) {
                    callback.onResponse(
                        this@NetworkResponseCall,
                        Response.success(PResult.Fail(FailState.default.copy(
                            message = e.message ?: "fail"
                        )))
                    )
                }
            }

            override fun onFailure(call: Call<T>, throwable: Throwable) {
                hLog("throwable >>> $throwable")
                val networkResponse: PResult<T> = when (throwable) {
                    else -> PResult.Fail(FailState.default.copy(
                        message = throwable.message ?: "fail"
                    ))
                }
                callback.onResponse(this@NetworkResponseCall, Response.success(networkResponse))
            }
        })
    }

    override fun isExecuted() = delegate.isExecuted

    override fun clone() = NetworkResponseCall(delegate.clone())

    override fun isCanceled() = delegate.isCanceled

    override fun cancel() = delegate.cancel()

    override fun execute(): Response<PResult<T>> {
        throw UnsupportedOperationException("NetworkResponseCall doesn't support execute")
    }

    override fun request(): Request = delegate.request()

    override fun timeout(): Timeout = delegate.timeout()
}