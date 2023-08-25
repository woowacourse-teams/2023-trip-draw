package com.teamtripdraw.android.support.framework.presentation.log

import com.teamtripdraw.android.support.BuildConfig.IS_RELEASE
import io.sentry.Sentry
import timber.log.Timber

object TripDrawLogUtil : LogUtil {

    private const val DEBUG_GENERAL_ERROR_LOG_FORMAT = "message : %s"
    private const val DEBUG_HTTP_CLIENT_ERROR_LOG = "DEBUG_HTTP_CLIENT_ERROR_LOG"
    private const val RELEASE_HTTP_CLIENT_ERROR_LOG = "DEBUG_HTTP_CLIENT_ERROR_LOG"
    private const val SENTRY_MESSAGE_KEY = "ERROR_MESSAGE"
    private const val NO_MESSAGE_NOTICE = "입력된 메시지가 없습니다."


//    override fun httpClient() {
//        when (IS_RELEASE) {
//            true -> httpClientReleaseErrorLog()
//            false -> httpClientDebugErrorLog()
//        }
//    }
//
//    private fun httpClientReleaseErrorLog() {
//        // todo Sentry 연결후 관련 로그 작성
//    }
//
//    private fun httpClientDebugErrorLog() {
//        Log.e(DEBUG_GENERAL_ERROR_LOG_FORMAT, functionName + (message ?: ""))
//    }

    override fun general(throwable: Throwable?, message: String?) {
        when (IS_RELEASE) {
            true -> generalReleaseErrorLog(throwable, message)
            false -> generalDebugErrorLog(throwable, message)
        }
    }

    private fun generalReleaseErrorLog(throwable: Throwable?, message: String?) {
        when (throwable == null) {
            true -> {
                Sentry.captureMessage(message ?: NO_MESSAGE_NOTICE)
            }
            false -> {
                Sentry.captureException(throwable) { scope ->
                    scope.setContexts(SENTRY_MESSAGE_KEY, message ?: NO_MESSAGE_NOTICE)
                }
            }
        }
    }

    private fun generalDebugErrorLog(throwable: Throwable?, message: String?) {
        when (throwable == null) {
            true -> Timber.e(DEBUG_GENERAL_ERROR_LOG_FORMAT.format(message ?: NO_MESSAGE_NOTICE))
            false -> Timber.e(
                throwable,
                DEBUG_GENERAL_ERROR_LOG_FORMAT.format(message ?: NO_MESSAGE_NOTICE),
            )
        }
    }
}
