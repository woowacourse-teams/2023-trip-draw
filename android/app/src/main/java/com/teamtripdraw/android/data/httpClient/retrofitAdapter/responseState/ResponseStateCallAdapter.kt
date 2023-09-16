package com.teamtripdraw.android.data.httpClient.retrofitAdapter.responseState

import com.teamtripdraw.android.data.httpClient.retrofitAdapter.responseState.enqueueActions.GeneralEnqueueActions
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.Type
import kotlin.reflect.KClass

class ResponseStateCallAdapter<R : Any>(
    private val responseType: Type,
    private val retrofit: Retrofit,
    private val enqueueActionsType: KClass<out GeneralEnqueueActions<*>>,
) :
    CallAdapter<R, Call<ResponseState<R>>> {
    override fun responseType(): Type = responseType

    @Suppress("UNCHECKED_CAST")
    override fun adapt(call: Call<R>): Call<ResponseState<R>> =
        ResponseStateCall(
            call,
            responseType,
            retrofit,
            enqueueActionsType as KClass<GeneralEnqueueActions<R>>,
        )
}
