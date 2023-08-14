package com.teamtripdraw.android.data.httpClient.retrofitAdapter

import android.util.Log
import okhttp3.Headers
import okhttp3.ResponseBody
import java.net.SocketTimeoutException
import java.net.UnknownHostException

sealed class ResponseState<out T : Any> {
    // Http 응답 code 200대(응답 성공)
    data class Success<T : Any>(val body: T, val headers: Headers) : ResponseState<T>()

    // isSuccessful의 값이 false인 경우(200~300대 응답이 아닌경우)
    data class Failure(val code: Int, val errorBody: ResponseBody? = null) :
        ResponseState<Nothing>()

    // 네트워크를 사용할수 없는 상태
    data class NetworkError(val error: UnknownHostException) : ResponseState<Nothing>()

    // OKHttp의 TimeOut설정 이상의 시간이 걸렸을 때 혹은 서버 지연이 발생했을때
    data class TimeOutError(val error: SocketTimeoutException) : ResponseState<Nothing>()

    // 위의 상황에 해당하지 않는 오류 상황
    data class UnknownError(val error: Throwable) : ResponseState<Nothing>()

    fun <R> process(
        failureListener: (code: Int, errorBody: ResponseBody?) -> Result<Nothing> = this::defaultFailureListener,
        networkErrorListener: (error: UnknownHostException) -> Result<Nothing> = this::defaultNetworkErrorListener,
        timeOutErrorListener: (error: SocketTimeoutException) -> Result<Nothing> = this::defaultTimeOutErrorListener,
        unknownErrorListener: (error: Throwable) -> Result<Nothing> = this::defaultUnknownErrorListener,
        successListener: (body: T, headers: Headers) -> Result<R>
    ): Result<R> {
        return when (this) {
            is Success -> {
                successListener(this.body, this.headers)
            }
            is Failure -> {
                failureListener(this.code, this.errorBody)
            }
            is NetworkError -> {
                networkErrorListener(this.error)
            }
            is TimeOutError -> {
                timeOutErrorListener(this.error)
            }
            is UnknownError -> {
                unknownErrorListener(this.error)
            }
        }
    }

    private fun defaultFailureListener(code: Int, errorBody: ResponseBody?): Result<Nothing> {
        Log.d("ResponseState.Failure", "code : $code, body : $errorBody")
        return Result.failure(
            IllegalAccessException(
                FAILURE_EXCEPTION_MESSAGE_FORMAT.format(code, errorBody?.string() ?: "")
            )
        )
    }

    private fun defaultNetworkErrorListener(error: UnknownHostException): Result<Nothing> {
        Log.e("ResponseState.NetworkError", "error : $error, message : ${error.message}")
        // 네트워크 연결 요청 액티비티 이동로직 혹은 네트워크 끊김 메시지 알림 로직-> 추후작성
        return Result.failure(error)
    }

    private fun defaultTimeOutErrorListener(error: SocketTimeoutException): Result<Nothing> {
        Log.e("ResponseState.TimeOutError", "error : $error, message : ${error.message}")
        return Result.failure(error)
    }

    private fun defaultUnknownErrorListener(error: Throwable): Result<Nothing> {
        Log.e("ResponseState.UnknownError", "error : $error, message : ${error.message}")
        return Result.failure(error)
    }

    companion object {
        private const val FAILURE_EXCEPTION_MESSAGE_FORMAT = "code : %d , errorBody: %s"
    }
}
