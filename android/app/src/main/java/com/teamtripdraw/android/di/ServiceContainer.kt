package com.teamtripdraw.android.di

import com.teamtripdraw.android.data.httpClient.service.SetNickNameService
import retrofit2.Retrofit

class ServiceContainer(retrofitContainer: RetrofitContainer) {
    val setNickNameService: SetNickNameService =
        retrofitContainer.tripDrawRetrofit.create(SetNickNameService::class.java)
}
