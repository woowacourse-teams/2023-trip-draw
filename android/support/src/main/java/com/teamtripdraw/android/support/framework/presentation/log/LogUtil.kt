package com.teamtripdraw.android.support.framework.presentation.log

interface LogUtil {
    fun general(throwable: Throwable?, message: String? = null)
    fun httpClient()
}
