package com.teamtripdraw.android.data.httpClient.retrofitAdapter.responseState

import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.reflect.Type

class ResponseStateCall<T : Any>(
    private val call: Call<T>,
    private val responseType: Type,
    private val retrofit: Retrofit,
) :
    Call<ResponseState<T>> {

    private val enqueueActions: EnqueueActions<T> =
        EnqueueActions(this, retrofit)

    override fun enqueue(callback: Callback<ResponseState<T>>) {
        call.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                val headers = response.headers()
                val body = response.body()
                val code = response.code()
                val errorBody = response.errorBody()

                if (response.isSuccessful) {
                    if (body != null) {
                        enqueueActions.successWithBodyAction(callback, headers, body)
                    } else {
                        if (responseType == Unit::class.java) {
                            enqueueActions.successWithOutBodyAction(callback, headers)
                        } else {
                            enqueueActions.unknownErrorByEmptyBodyAction(callback)
                        }
                    }
                } else {
                    enqueueActions.responseFailureAction(callback, code, errorBody)
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                enqueueActions.requestFailureAction(t, callback)
            }
        })
    }

    override fun execute(): Response<ResponseState<T>> {
        throw UnsupportedOperationException("해당 커스텀 callAdpater에서는 execute를 지원하지 않습니다.")
    }

    override fun clone(): Call<ResponseState<T>> =
        ResponseStateCall(call.clone(), responseType, retrofit)

    override fun isExecuted(): Boolean = call.isExecuted

    override fun cancel() = call.cancel()

    override fun isCanceled(): Boolean = call.isCanceled

    override fun request(): Request = call.request()

    override fun timeout(): Timeout = call.timeout()
}
