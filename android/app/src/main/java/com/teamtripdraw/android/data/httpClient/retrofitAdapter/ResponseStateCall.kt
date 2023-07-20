package com.teamtripdraw.android.data.httpClient.retrofitAdapter

import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ResponseStateCall<T : Any>(private val call: Call<T>, private val responseType: Type) :
    Call<ResponseState<T>> {

    override fun enqueue(callback: Callback<ResponseState<T>>) {
        call.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                val headers = response.headers()
                val body = response.body()
                val code = response.code()
                val errorBody = response.errorBody()

                if (response.isSuccessful) {
                    if (body != null) {
                        callback.onResponse(
                            this@ResponseStateCall,
                            Response.success(ResponseState.Success(body, headers))
                        )
                    } else {
                        if (responseType == Unit::class.java) {
                            callback.onResponse(
                                this@ResponseStateCall,
                                Response.success(ResponseState.Success(Unit as T, headers))
                            )
                        } else {
                            callback.onResponse(
                                this@ResponseStateCall,
                                Response.success(
                                    ResponseState.UnknownError(
                                        IllegalArgumentException(
                                            RETURN_TYPE_NOT_UNIT_ERROR
                                        )
                                    )
                                )
                            )
                        }
                    }
                } else {
                    callback.onResponse(
                        this@ResponseStateCall,
                        Response.success(ResponseState.Failure(code, errorBody))
                    )
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                val errorResponse = when (t) {
                    // UnknownHostException 네트워크 연결 상태 확인 코드 추후 처리
                    is UnknownHostException -> ResponseState.NetworkError(t)
                    is SocketTimeoutException -> ResponseState.TimeOutError(t)
                    else -> ResponseState.UnknownError(t)
                }
                callback.onResponse(this@ResponseStateCall, Response.success(errorResponse))
            }
        })
    }

    override fun execute(): Response<ResponseState<T>> {
        throw UnsupportedOperationException("해당 커스텀 callAdpater에서는 execute를 지원하지 않습니다.")
    }

    override fun clone(): Call<ResponseState<T>> = ResponseStateCall(call.clone(), responseType)

    override fun isExecuted(): Boolean = call.isExecuted

    override fun cancel() = call.cancel()

    override fun isCanceled(): Boolean = call.isCanceled

    override fun request(): Request = call.request()

    override fun timeout(): Timeout = call.timeout()

    companion object {
        private const val RETURN_TYPE_NOT_UNIT_ERROR =
            "Body가 존재하지 않지만, Unit 이외의 타입으로 정의했습니다. ResponseState<Unit>로 정의하세요"
    }
}
