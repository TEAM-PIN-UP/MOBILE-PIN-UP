package com.pinup.pinup.remote.network

import com.pinup.pinup.domain.model.PResult
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

class NetworkResponseAdapter<T>(
    private val successType: Type,
) : CallAdapter<T, Call<PResult<T>>> { // 여기서 <앞, 뒤> 에 넣어준 것에 따라

    override fun responseType(): Type = successType

    override fun adapt(call: Call<T>): Call<PResult<T>> = NetworkResponseCall(call)
}