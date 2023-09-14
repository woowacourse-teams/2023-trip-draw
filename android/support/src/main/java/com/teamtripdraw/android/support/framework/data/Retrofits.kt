package com.teamtripdraw.android.support.framework.data

import okhttp3.ResponseBody
import retrofit2.Retrofit

inline fun <reified T> Retrofit.getParsedErrorBody(errorBody: ResponseBody?): T? =
    errorBody?.let {
        responseBodyConverter<T>(
            T::class.java,
            T::class.java.annotations,
        ).convert(errorBody)
    }
