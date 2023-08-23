package com.teamtripdraw.android.support.framework.presentation.log

interface LogUtil {
    fun httpClient()
    fun general(className: String, functionName: String, message: String? = null)
}
