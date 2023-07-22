package com.teamtripdraw.android.di

import com.teamtripdraw.android.data.httpClient.service.NicknameSetupService

class ServiceContainer(retrofitContainer: RetrofitContainer) {
    val nicknameSetupService: NicknameSetupService =
        retrofitContainer.tripDrawRetrofit.create(NicknameSetupService::class.java)
}
