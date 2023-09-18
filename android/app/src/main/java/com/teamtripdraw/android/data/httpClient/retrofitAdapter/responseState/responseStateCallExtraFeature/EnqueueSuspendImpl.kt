package com.teamtripdraw.android.data.httpClient.retrofitAdapter.responseState.responseStateCallExtraFeature

import com.teamtripdraw.android.TripDrawApplication.DependencyContainer.logUtil
import com.teamtripdraw.android.data.httpClient.retrofitAdapter.responseState.ResponseState
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.coroutines.resume

class EnqueueSuspendImpl<T : Any>(private val call: Call<T>, private val responseType: Type) :
    EnqueueSuspend<T> {

    @Suppress("UNCHECKED_CAST")
    override suspend fun enqueueSuspend(): ResponseState<T> =
        suspendCancellableCoroutine { continuation ->
            call.enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val headers = response.headers()
                    val body = response.body()
                    val code = response.code()
                    val errorBody = response.errorBody()

                    if (response.isSuccessful) {
                        if (body != null) {
                            continuation.resume(ResponseState.Success(body, headers))
                        } else {
                            if (responseType == Unit::class.java) {
                                continuation.resume(ResponseState.Success(Unit as T, headers))
                            } else {
                                continuation.resume(
                                    ResponseState.UnknownError(
                                        IllegalStateException(RETURN_TYPE_NOT_UNIT_ERROR),
                                    ),
                                )
                            }
                        }
                    } else {
                        continuation.resume(ResponseState.Failure(code, errorBody))
                    }
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    when (t) {
                        is UnknownHostException -> {
                            continuation.resume(ResponseState.NetworkError(t))
                        }
                        is SocketTimeoutException -> {
                            continuation.resume(ResponseState.TimeOutError(t))
                        }
                        else -> {
                            continuation.resume(ResponseState.UnknownError(t))
                        }
                    }
                }
            })

            continuation.invokeOnCancellation {
                logUtil.general.log(it, ENQUEUE_SUSPEND_CANCEL_MESSAGE)
            }
        }

    companion object {
        private const val ENQUEUE_SUSPEND_CANCEL_MESSAGE = "enqueueSuspend를 실행도중 Cancel이 발생하였습니다."
        private const val RETURN_TYPE_NOT_UNIT_ERROR =
            "enqueueSuspend 함수 실행중 결과로 Body가 존재하지 않지만, Unit 이외의 타입으로 정의했습니다. ResponseState<Unit>로 정의하세요"
    }
}
