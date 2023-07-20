package com.teamtripdraw.android.data.httpClient.retrofitAdapter

import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

class ResponseStateCallAdapter<R : Any>(private val responseType: Type) :
    CallAdapter<R, Call<ResponseState<R>>> {
    override fun responseType(): Type = responseType

    override fun adapt(call: Call<R>): Call<ResponseState<R>> =
        ResponseStateCall(call, responseType)
}
