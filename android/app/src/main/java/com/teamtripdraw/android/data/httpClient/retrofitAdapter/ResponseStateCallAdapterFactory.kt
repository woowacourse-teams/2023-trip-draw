package com.teamtripdraw.android.data.httpClient.retrofitAdapter

import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class ResponseStateCallAdapterFactory : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit,
    ): CallAdapter<*, *>? {
        if (Call::class.java != getRawType((returnType))) {
            return null
        }

        check(returnType is ParameterizedType) {
            "return type must be parameterized as Call<ResponseState<Foo>> or Call<ResponseState<out Foo>>"
        }

        val responseType = getParameterUpperBound(0, returnType)

        if (getRawType(responseType) != ResponseState::class.java) {
            return null
        }

        check(responseType is ParameterizedType) {
            "Response must be parameterized as ResponseState<Foo> or ResponseState<out Foo>"
        }

        val bodyType = getParameterUpperBound(0, responseType)

        return ResponseStateCallAdapter<Any>(bodyType)
    }
}
