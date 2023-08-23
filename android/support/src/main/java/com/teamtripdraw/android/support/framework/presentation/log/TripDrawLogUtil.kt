package com.teamtripdraw.android.support.framework.presentation.log

import android.util.Log
import com.teamtripdraw.android.support.BuildConfig.IS_RELEASE

object TripDrawLogUtil : LogUtil {

    private const val DEBUG_GENERAL_ERROR_LOG = "DEBUG_GENERAL_ERROR_LOG_%s"
    private const val RELEASE_GENERAL_ERROR_LOG = "RELEASE_GENERAL_ERROR_LOG_%s"
    private const val DEBUG_HTTP_CLIENT_ERROR_LOG = "DEBUG_HTTP_CLIENT_ERROR_LOG_%s"
    private const val RELEASE_HTTP_CLIENT_ERROR_LOG = "DEBUG_HTTP_CLIENT_ERROR_LOG_%s"
    private const val CLASS_NAME = "CLASS_NAME"
    private const val MESSAGE = "MESSAGE"

    override fun httpClient() {
        // todo Sentry 연결후 관련 로그 작성
    }

    override fun general(className: String, functionName: String, message: String?) {
        when (IS_RELEASE) {
            true -> generalReleaseErrorLog(className, functionName, message)
            false -> generalDebugErrorLog(className, functionName, message)
        }
    }

    private fun generalReleaseErrorLog(className: String, functionName: String, message: String?) {
        // todo Sentry 연결후 관련 로그 작성
    }

    private fun generalDebugErrorLog(className: String, functionName: String, message: String?) {
        // todo Sentry 연결후 관련 로그 작성
        Log.e(DEBUG_GENERAL_ERROR_LOG, functionName + (message ?: ""))
    }
}
