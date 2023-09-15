package com.teamtripdraw.android.data.httpClient.retrofitAdapter.responseState

import com.teamtripdraw.android.data.httpClient.dto.failureResponse.GeneralFailureResponse
import com.teamtripdraw.android.data.httpClient.retrofitAdapter.TokenExpiryType
import com.teamtripdraw.android.support.framework.data.getParsedErrorBody
import okhttp3.Headers
import okhttp3.ResponseBody
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class EnqueueActions<T : Any>(
    private val responseStateCall: ResponseStateCall<T>,
    private val retrofit: Retrofit,
) {

    fun successWithBodyAction(
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
    fun successWithOutBodyAction(
        callback: Callback<ResponseState<T>>,
        headers: Headers,
    ) {
        callback.onResponse(
            responseStateCall,
            Response.success(ResponseState.Success(Unit as T, headers)),
        )
    }

    fun unknownErrorByEmptyBodyAction(callback: Callback<ResponseState<T>>) {
        callback.onResponse(
            responseStateCall,
            Response.success(
                ResponseState.UnknownError(IllegalArgumentException(RETURN_TYPE_NOT_UNIT_ERROR)),
            ),
        )
    }

    fun responseFailureAction(
        callback: Callback<ResponseState<T>>,
        code: Int,
        errorBody: ResponseBody?,
    ) {
        if (code == TOKEN_EXPIRED_CODE) {
            when (getTokenExpiryType(errorBody)) {
                TokenExpiryType.REFRESH_TOKEN -> refreshTokenExpiredAction()
                TokenExpiryType.ACCESS_TOKEN -> accessTokenExpiredAction()
            }
        } else {
            generalResponseFailureAction(callback, code, errorBody)
        }
    }

    private fun getTokenExpiryType(errorBody: ResponseBody?): TokenExpiryType {
        val generalFailureResponse =
            retrofit.getParsedErrorBody<GeneralFailureResponse>(errorBody)
                ?: throw IllegalStateException(EXPIRED_TOKEN_RESPONSE_HAS_NO_ERROR_BODY)
        val exceptionCode = generalFailureResponse.exceptionCode
        return TokenExpiryType.getByServerExceptionCode(exceptionCode)
    }

    private fun accessTokenExpiredAction() {
    }

    private fun refreshTokenExpiredAction() {
    }

    private fun generalResponseFailureAction(
        callback: Callback<ResponseState<T>>,
        code: Int,
        errorBody: ResponseBody?,
    ) {
        callback.onResponse(
            responseStateCall,
            Response.success(ResponseState.Failure(code, errorBody)),
        )
    }

    fun requestFailureAction(
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
        private const val EXPIRED_TOKEN_RESPONSE_HAS_NO_ERROR_BODY =
            "토큰 에러 관련 에러바디의 값이 null값입니다"
        private const val TOKEN_EXPIRED_CODE = 401
    }
}
