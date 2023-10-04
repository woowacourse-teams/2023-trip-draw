package com.teamtripdraw.android.support.framework.presentation.log.tripDrawLogUtil

interface CrashlyticsHandler {
    fun recordException(throwable: Throwable, keyName: String, keyValue: String)
}
