package com.teamtripdraw.android.data.httpClient.retrofitAdapter

import com.teamtripdraw.android.data.httpClient.dto.failureResponse.GeneralFailureResponse
import okhttp3.Headers
import okhttp3.Request
import okhttp3.ResponseBody
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ResponseStateCall<T : Any>(
    private val call: Call<T>,
    private val responseType: Type,
    private val jsonConverter: JsonConverter,
) :
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
                        successWithBodyProcess(callback, headers, body)
                    } else {
                        if (responseType == Unit::class.java) {
                            successWithOutBodyProcess(callback, headers)
                        } else {
                            unknownErrorByEmptyBodyProcess(callback)
                        }
                    }
                } else {
                    requestFailureProcess(callback, code, errorBody)
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                val errorResponse = when (t) {
                    // todo UnknownHostException 네트워크 연결 상태 확인 코드 추후 처리
                    is UnknownHostException -> ResponseState.NetworkError(t)
                    is SocketTimeoutException -> ResponseState.TimeOutError(t)
                    else -> ResponseState.UnknownError(t)
                }
                callback.onResponse(this@ResponseStateCall, Response.success(errorResponse))
            }
        })
    }

    private fun successWithBodyProcess(
        callback: Callback<ResponseState<T>>,
        headers: Headers,
        body: T,
    ) {
        callback.onResponse(
            this@ResponseStateCall,
            Response.success(ResponseState.Success(body, headers)),
        )
    }

    @Suppress("UNCHECKED_CAST")
    private fun successWithOutBodyProcess(
        callback: Callback<ResponseState<T>>,
        headers: Headers,
    ) {
        callback.onResponse(
            this@ResponseStateCall,
            Response.success(ResponseState.Success(Unit as T, headers)),
        )
    }

    private fun unknownErrorByEmptyBodyProcess(callback: Callback<ResponseState<T>>) {
        callback.onResponse(
            this@ResponseStateCall,
            Response.success(
                ResponseState.UnknownError(IllegalArgumentException(RETURN_TYPE_NOT_UNIT_ERROR)),
            ),
        )
    }

    private fun requestFailureProcess(
        callback: Callback<ResponseState<T>>,
        code: Int,
        errorBody: ResponseBody?,
    ) {
        if (code == TOKEN_EXPIRED_CODE) {
            when (getTokenExpiryType(errorBody)) {
                TokenExpiryType.REFRESH_TOKEN -> refreshTokenExpiredProcess()
                TokenExpiryType.ACCESS_TOKEN -> accessTokenExpiredProcess()
            }
        } else {
            generalRequestFailureProcess(callback, code, errorBody)
        }
    }

    private fun getTokenExpiryType(errorBody: ResponseBody?): TokenExpiryType {
        val generalFailureResponse =
            jsonConverter.toKotlinClass<GeneralFailureResponse>(
                errorBody?.string() ?: throw IllegalStateException(
                    EXPIRED_TOKEN_RESPONSE_HAS_NO_ERROR_BODY,
                ),
                GeneralFailureResponse::class.java,
            ) ?: throw IllegalStateException(EXPIRED_TOKEN_RESPONSE_HAS_NO_ERROR_BODY)
        val exceptionCode = generalFailureResponse.exceptionCode
        return TokenExpiryType.getByServerExceptionCode(exceptionCode)
    }

    private fun accessTokenExpiredProcess() {
    }

    private fun refreshTokenExpiredProcess() {
    }

    private fun generalRequestFailureProcess(
        callback: Callback<ResponseState<T>>,
        code: Int,
        errorBody: ResponseBody?,
    ) {
        callback.onResponse(
            this@ResponseStateCall,
            Response.success(ResponseState.Failure(code, errorBody)),
        )
    }

    override fun execute(): Response<ResponseState<T>> {
        throw UnsupportedOperationException("해당 커스텀 callAdpater에서는 execute를 지원하지 않습니다.")
    }

    override fun clone(): Call<ResponseState<T>> =
        ResponseStateCall(call.clone(), responseType, jsonConverter)

    override fun isExecuted(): Boolean = call.isExecuted

    override fun cancel() = call.cancel()

    override fun isCanceled(): Boolean = call.isCanceled

    override fun request(): Request = call.request()

    override fun timeout(): Timeout = call.timeout()

    companion object {
        private const val RETURN_TYPE_NOT_UNIT_ERROR =
            "Body가 존재하지 않지만, Unit 이외의 타입으로 정의했습니다. ResponseState<Unit>로 정의하세요"
        private const val EXPIRED_TOKEN_RESPONSE_HAS_NO_ERROR_BODY =
            "토큰 에러 관련 에러바디의 값이 null값입니다"
        private const val TOKEN_EXPIRED_CODE = 401
    }
}
