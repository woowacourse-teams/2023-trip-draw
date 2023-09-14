package com.teamtripdraw.android.data.httpClient.retrofitAdapter

interface JsonConverter {
    fun <T : Any> toKotlinClass(errorBody: String, clazz: Class<T>): T?
}
