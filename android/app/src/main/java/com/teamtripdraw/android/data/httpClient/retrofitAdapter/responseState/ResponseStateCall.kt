package com.teamtripdraw.android.data.httpClient.retrofitAdapter.responseState

import com.teamtripdraw.android.data.httpClient.retrofitAdapter.responseState.enqueueActions.GeneralEnqueueActions
import com.teamtripdraw.android.data.httpClient.retrofitAdapter.responseState.responseStateCallExtraFeature.EnqueueSuspend
import com.teamtripdraw.android.data.httpClient.retrofitAdapter.responseState.responseStateCallExtraFeature.EnqueueSuspendImpl
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.reflect.Type
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.primaryConstructor

class ResponseStateCall<T : Any>(
    private val call: Call<T>,
    private val responseType: Type,
    private val retrofit: Retrofit,
    private val enqueueActionsType: KClass<GeneralEnqueueActions<T>>,
    private val enqueueActionParameters: List<Any>,
) : Call<ResponseState<T>>, EnqueueSuspend<T> by EnqueueSuspendImpl(call, responseType) {

    private val enqueueActions: GeneralEnqueueActions<T> = initEnqueueActions()

    private fun initEnqueueActions():
        GeneralEnqueueActions<T> {
        val enqueueActionsPrimaryConstructor: KFunction<GeneralEnqueueActions<T>> =
            enqueueActionsType.primaryConstructor ?: throw IllegalStateException(
                NULL_ENQUEUE_ACTIONS_PRIMARY_CONSTRUCTOR,
            )
        val enqueueActionParametersWithResponseStateCall: List<Any> =
            enqueueActionParameters.toMutableList().apply { add(0, this@ResponseStateCall) }
        return enqueueActionsPrimaryConstructor.call(*enqueueActionParametersWithResponseStateCall.toTypedArray())
    }

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
        ResponseStateCall(
            call.clone(),
            responseType,
            retrofit,
            enqueueActionsType,
            enqueueActionParameters,
        )

    override fun isExecuted(): Boolean = call.isExecuted

    override fun cancel() = call.cancel()

    override fun isCanceled(): Boolean = call.isCanceled

    override fun request(): Request = call.request()

    override fun timeout(): Timeout = call.timeout()

    companion object {
        private const val NULL_ENQUEUE_ACTIONS_PRIMARY_CONSTRUCTOR =
            "입력하신 EnqueueActions 클래스의 primary 생성자가 null값입니다.EnqueueActions 주생성자를 확인하세요"
    }
}
