package com.pinup.pinup.remote.network

import android.util.Log
import com.pinup.pinup.domain.model.PResult
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class NetworkCallAdapterFactory : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        // check first that the return type is `ParameterizedType`
        // returnType이 제네릭 인자를 가지는지? Call<NetworkResponse<<Foo>> or Call<NetworkResponse<out Foo>>
        check(returnType is ParameterizedType) {
            "return type must be parameterized as Call<NetworkResponse<<Foo>> or Call<NetworkResponse<out Foo>>"
        }

        // get the response type inside the `Call` type
        // returnType에서 첫번째 제네릭 인자를 얻는다. NetworkResponse<out Foo>
        val responseType = getParameterUpperBound(0, returnType)
        // if the response type is not ApiResponse then we can't handle this type, so we return null
        // 기대한 것처럼 동작하기 위해서는 추출한 제네릭 인자가 내가 만든 NetworkResponse타입이어야함.
        if (getRawType(responseType) != PResult::class.java) {
            return null
        }

        // the response type is ApiResponse and should be parameterized
        // 제네릭 인자 가지는지 확인 NetworkResponse<Foo> or NetworkResponse<out Foo>
        check(responseType is ParameterizedType) { "Response must be parameterized as NetworkResponse<Foo> or NetworkResponse<out Foo>" }
        // Foo를 얻어서 CallAdapter를 생성한다.
        val successBodyType = getParameterUpperBound(0, responseType)
        return NetworkResponseAdapter<Any>(successBodyType)
    }
}