package com.teamtripdraw.android.data.httpClient.retrofitAdapter.responseState.enqueueActions

import com.teamtripdraw.android.data.httpClient.retrofitAdapter.ServerType.*
import com.teamtripdraw.android.data.httpClient.retrofitAdapter.responseState.ResponseState
import com.teamtripdraw.android.data.httpClient.retrofitAdapter.responseState.ResponseStateCall
import okhttp3.Headers
import okhttp3.ResponseBody
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * EnqueueActions을 커스텀 하고싶다면 상속받아 커스텀 Actions를 만들어 사용합니다.
 * 이때 주의점으로 주생성자 내에서 responseStateCall,retrofit의 순서를 지켜주셔야 합니다.
 *
 * @param responseStateCall: 결과처리를 위한 ResponseStateCall 객체 자체를 뜻합니다.
 * @param retrofit: GeneralEnqueueActions에서는 사용하지 않지만 에러바디 컨버팅 혹은 여타 오류처리를 위해 제공합니다.
 *
 * 다른 추가적인 주생성자 매개변수의 경우 앞서 설명한 두 매개변수 다음에 위치하도록 순서에 맞춰서 추가해주시길 바랍니다.
 */
open class GeneralEnqueueActions<T : Any>(
    protected val responseStateCall: ResponseStateCall<T>,
    protected val retrofit: Retrofit,
) {
    open fun successWithBodyAction(
        callback: Callback<ResponseState<T>>,
        headers: Headers,
        body: T,
    ) {
        callback.onResponse(
            responseStateCall,
            Response.success(ResponseState.Success(body, headers)),
        )
    }

    @Suppress("UNCHECKED_CAST")
    open fun successWithOutBodyAction(
        callback: Callback<ResponseState<T>>,
        headers: Headers,
    ) {
        callback.onResponse(
            responseStateCall,
            Response.success(ResponseState.Success(Unit as T, headers)),
        )
    }

    open fun unknownErrorByEmptyBodyAction(callback: Callback<ResponseState<T>>) {
        callback.onResponse(
            responseStateCall,
            Response.success(
                ResponseState.UnknownError(IllegalArgumentException(RETURN_TYPE_NOT_UNIT_ERROR)),
            ),
        )
    }

    open fun responseFailureAction(
        callback: Callback<ResponseState<T>>,
        code: Int,
        errorBody: ResponseBody?,
    ) {
        callback.onResponse(
            responseStateCall,
            Response.success(ResponseState.Failure(code, errorBody)),
        )
    }

    open fun requestFailureAction(
        throwable: Throwable,
        callback: Callback<ResponseState<T>>,
    ) {
        val errorResponse = when (throwable) {
            is UnknownHostException -> networkErrorAction(throwable)
            is SocketTimeoutException -> timeOutErrorAction(throwable)
            else -> ResponseState.UnknownError(throwable)
        }
        callback.onResponse(responseStateCall, Response.success(errorResponse))
    }

    private fun networkErrorAction(unknownHostException: UnknownHostException): ResponseState.NetworkError {
        // todo UnknownHostException 네트워크 연결 상태 확인 코드 추가 및 관련 에러처리
        return ResponseState.NetworkError(unknownHostException)
    }

    private fun timeOutErrorAction(socketTimeoutException: SocketTimeoutException): ResponseState.TimeOutError {
        return ResponseState.TimeOutError(socketTimeoutException)
    }

    companion object {
        private const val RETURN_TYPE_NOT_UNIT_ERROR =
            "Body가 존재하지 않지만, Unit 이외의 타입으로 정의했습니다. ResponseState<Unit>로 정의하세요"
    }
}
