package com.teamtripdraw.android.di

import com.teamtripdraw.android.data.httpClient.service.GetNicknameService
import com.teamtripdraw.android.data.httpClient.service.NicknameSetupService

class ServiceContainer(retrofitContainer: RetrofitContainer) {
    val nicknameSetupService: NicknameSetupService =
        retrofitContainer.tripDrawRetrofit.create(NicknameSetupService::class.java)
    val getNicknameService: GetNicknameService =
        retrofitContainer.tripDrawRetrofit.create(GetNicknameService::class.java)
}
