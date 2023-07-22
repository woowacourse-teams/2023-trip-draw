package com.teamtripdraw.android.di

import com.teamtripdraw.android.data.httpClient.service.SetNickNameService

class ServiceContainer(retrofitContainer: RetrofitContainer) {
    val setNickNameService: SetNickNameService =
        retrofitContainer.tripDrawRetrofit.create(SetNickNameService::class.java)
}
