package com.teamtripdraw.android.support.framework.presentation.log.tripDrawLogUtil

import CrashlyticsHandlerImpl
import com.teamtripdraw.android.support.BuildConfig.IS_RELEASE
import com.teamtripdraw.android.support.framework.presentation.log.LogUtil
import io.sentry.Sentry
import okhttp3.ResponseBody
import timber.log.Timber
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class TripDrawLogUtil() : LogUtil {
    override val general: LogUtil.General = object : LogUtil.General {

        override fun log(throwable: Throwable?, message: String?) {
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
                true -> Timber.e(
                    DEBUG_GENERAL_ERROR_LOG_FORMAT.format(
                        message ?: NO_MESSAGE_NOTICE,
                    ),
                )
                false -> Timber.e(
                    throwable,
                    DEBUG_GENERAL_ERROR_LOG_FORMAT.format(message ?: NO_MESSAGE_NOTICE),
                )
            }
        }
    }

    override val httpClient: LogUtil.HttpClient = object : LogUtil.HttpClient {

        private val crashlyticsHandler: CrashlyticsHandler = CrashlyticsHandlerImpl()

        override fun failure(code: Int, errorBody: ResponseBody?) {
            when (IS_RELEASE) {
                true -> failureReleaseLog(code, errorBody)
                false -> failureDebugLog(code, errorBody)
            }
        }

        private fun failureReleaseLog(code: Int, errorBody: ResponseBody?) {
            Sentry.captureMessage(SENTRY_RESPONSE_STATE_FAILURE_MESSAGE) { scope ->
                scope.setTag(SENTRY_HTTP_ERROR_TAG_KEY, RESPONSE_STATE_FAILURE)
                scope.setContexts(RESPONSE_STATE_FAILURE_CODE_KEY, code)
                scope.setContexts(
                    RESPONSE_STATE_FAILURE_ERROR_BODY_KEY,
                    errorBody?.string() ?: NO_ERROR_BODY_MESSAGE,
                )
            }
        }

        private fun failureDebugLog(code: Int, errorBody: ResponseBody?) {
            Timber.e(DEBUG_RESPONSE_STATE_FAILURE.format(code, errorBody ?: NO_ERROR_BODY_MESSAGE))
        }

        override fun network(error: UnknownHostException) {
            when (IS_RELEASE) {
                true -> networkReleaseLog(error)
                false -> networkDebugLog(error)
            }
        }

        private fun networkReleaseLog(error: UnknownHostException) {
            crashlyticsHandler.recordException(
                error,
                CRASHLYTICS_HTTP_ERROR_KEY_NAME,
                RESPONSE_STATE_NETWORK,
            )
        }

        private fun networkDebugLog(error: UnknownHostException) {
            Timber.e(error, RESPONSE_STATE_NETWORK)
        }

        override fun timeOut(error: SocketTimeoutException) {
            when (IS_RELEASE) {
                true -> timeOutReleaseLog(error)
                false -> timeOutDebugLog(error)
            }
        }

        private fun timeOutReleaseLog(error: SocketTimeoutException) {
            Sentry.captureException(error) { scope ->
                scope.setTag(SENTRY_HTTP_ERROR_TAG_KEY, RESPONSE_STATE_TIME_OUT)
            }
        }

        private fun timeOutDebugLog(error: SocketTimeoutException) {
            Timber.e(error, RESPONSE_STATE_TIME_OUT)
        }

        override fun unknown(error: Throwable) {
            when (IS_RELEASE) {
                true -> unknownReleaseLog(error)
                false -> unknownDebugLog(error)
            }
        }

        private fun unknownReleaseLog(error: Throwable) {
            Sentry.captureException(error) { scope ->
                scope.setTag(SENTRY_HTTP_ERROR_TAG_KEY, RESPONSE_STATE_UNKNOWN)
            }
        }

        private fun unknownDebugLog(error: Throwable) {
            Timber.e(error, RESPONSE_STATE_UNKNOWN)
        }
    }

    companion object {
        private const val DEBUG_GENERAL_ERROR_LOG_FORMAT = "message : %s"
        private const val SENTRY_MESSAGE_KEY = "ERROR_MESSAGE"
        private const val NO_MESSAGE_NOTICE = "입력된 메시지가 없습니다."
        private const val SENTRY_HTTP_ERROR_TAG_KEY = "HTTP_ERROR"
        private const val CRASHLYTICS_HTTP_ERROR_KEY_NAME = SENTRY_HTTP_ERROR_TAG_KEY
        private const val DEBUG_RESPONSE_STATE_FAILURE =
            "RESPONSE_STATE_FAILURE code: %d errorBody: %s"
        private const val SENTRY_RESPONSE_STATE_FAILURE_MESSAGE = "HTTP_FAILURE"
        private const val RESPONSE_STATE_FAILURE = "RESPONSE_STATE_FAILURE"
        private const val RESPONSE_STATE_FAILURE_CODE_KEY = "code"
        private const val RESPONSE_STATE_FAILURE_ERROR_BODY_KEY = "error body"
        private const val NO_ERROR_BODY_MESSAGE = "에러 바디가 없습니다."
        private const val RESPONSE_STATE_NETWORK = "RESPONSE_STATE_NETWORK"
        private const val RESPONSE_STATE_TIME_OUT = "RESPONSE_STATE_TIME_OUT"
        private const val RESPONSE_STATE_UNKNOWN = "RESPONSE_STATE_UNKNOWN"
    }
}
