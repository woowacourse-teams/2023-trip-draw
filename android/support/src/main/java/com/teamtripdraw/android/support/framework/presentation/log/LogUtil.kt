package com.teamtripdraw.android.support.framework.presentation.log

import okhttp3.ResponseBody
import java.net.SocketTimeoutException
import java.net.UnknownHostException

interface LogUtil {

    val general: General

    interface General {
        fun log(throwable: Throwable?, message: String? = null)
    }

    val httpClient: HttpClient

    interface HttpClient {
        fun failure(code: Int, errorBody: ResponseBody?)
        fun network(error: UnknownHostException)
        fun timeOut(error: SocketTimeoutException)
        fun unknown(error: Throwable)
    }
}
