package com.teamtripdraw.android.di

import com.teamtripdraw.android.data.httpClient.service.CreateRecordingPointService
import com.teamtripdraw.android.data.httpClient.service.CreateTripService
import com.teamtripdraw.android.data.httpClient.service.GetNicknameService
import com.teamtripdraw.android.data.httpClient.service.GetPointService
import com.teamtripdraw.android.data.httpClient.service.NicknameSetupService
import com.teamtripdraw.android.data.httpClient.service.PostService
import retrofit2.create

class ServiceContainer(retrofitContainer: RetrofitContainer) {
    val nicknameSetupService: NicknameSetupService =
        retrofitContainer.tripDrawRetrofit.create(NicknameSetupService::class.java)
    val getNicknameService: GetNicknameService =
        retrofitContainer.tripDrawRetrofit.create(GetNicknameService::class.java)
    val createTripService: CreateTripService =
        retrofitContainer.tripDrawRetrofit.create(CreateTripService::class.java)
    val postService: PostService =
        retrofitContainer.tripDrawRetrofit.create(PostService::class.java)
    val createRecordingPointService: CreateRecordingPointService =
        retrofitContainer.tripDrawRetrofit.create(CreateRecordingPointService::class.java)
    val getPointService: GetPointService =
        retrofitContainer.tripDrawRetrofit.create(GetPointService::class.java)
}
