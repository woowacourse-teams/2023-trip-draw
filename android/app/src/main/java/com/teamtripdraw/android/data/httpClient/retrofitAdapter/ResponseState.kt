package com.teamtripdraw.android.data.httpClient.retrofitAdapter

import okhttp3.ResponseBody
import java.net.SocketTimeoutException
import java.net.UnknownHostException

sealed class ResponseState<out T> {
    // Http 응답 code 200대(응답 성공)
    data class Success<T>(val body: T) : ResponseState<T>()

    // isSuccessful의 값이 false인 경우(200~300대 응답이 아닌경우)
    data class Failure(val code: Int, val errorBody: ResponseBody? = null) :
        ResponseState<Nothing>()

    // 네트워크를 사용할수 없는 상태
    data class NetworkError(val error: UnknownHostException) : ResponseState<Nothing>()

    // OKHttp의 TimeOut설정 이상의 시간이 걸렸을 때 혹은 서버 지연이 발생했을때
    data class TimeOutError(val error: SocketTimeoutException) : ResponseState<Nothing>()

    // 위의 상황에 해당하지 않는 오류 상황
    data class UnknownError(val error: Throwable) : ResponseState<Nothing>()
}
