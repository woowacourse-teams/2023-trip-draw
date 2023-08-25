package com.teamtripdraw.android.support.framework.presentation.log

import okhttp3.ResponseBody
import java.net.SocketTimeoutException
import java.net.UnknownHostException

interface LogUtil {
    fun general(throwable: Throwable?, message: String? = null)
    interface HttpClient {
        fun failure(code: Int, errorBody: ResponseBody?)
        fun network(error: UnknownHostException)
        fun timeOut(error: SocketTimeoutException)
        fun unknown(error: Throwable)
    }
}
