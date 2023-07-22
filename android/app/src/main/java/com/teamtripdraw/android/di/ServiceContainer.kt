package com.teamtripdraw.android.di

import com.teamtripdraw.android.data.httpClient.service.SetNickNameService
import retrofit2.Retrofit

class ServiceContainer(retrofit: Retrofit) {
    val setNickNameService: SetNickNameService =
        retrofit.create(SetNickNameService::class.java)
}
