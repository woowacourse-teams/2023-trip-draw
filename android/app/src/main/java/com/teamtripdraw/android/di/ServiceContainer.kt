package com.teamtripdraw.android.di

import com.teamtripdraw.android.data.httpClient.service.CreateRecordingPointService
import com.teamtripdraw.android.data.httpClient.service.CreateTripService
import com.teamtripdraw.android.data.httpClient.service.DeletePointService
import com.teamtripdraw.android.data.httpClient.service.DeleteTripService
import com.teamtripdraw.android.data.httpClient.service.GetAllTripsService
import com.teamtripdraw.android.data.httpClient.service.GetNicknameService
import com.teamtripdraw.android.data.httpClient.service.GetPointService
import com.teamtripdraw.android.data.httpClient.service.GetTripInfoService
import com.teamtripdraw.android.data.httpClient.service.NicknameSetupService
import com.teamtripdraw.android.data.httpClient.service.PostService
import com.teamtripdraw.android.data.httpClient.service.SetTripTitleService

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
    val getTripInfoService: GetTripInfoService =
        retrofitContainer.tripDrawRetrofit.create(GetTripInfoService::class.java)
    val getPointService: GetPointService =
        retrofitContainer.tripDrawRetrofit.create(GetPointService::class.java)
    val deletePointService: DeletePointService =
        retrofitContainer.tripDrawRetrofit.create(DeletePointService::class.java)
    val setTripTitleService: SetTripTitleService =
        retrofitContainer.tripDrawRetrofit.create(SetTripTitleService::class.java)
    val getAllTripsService: GetAllTripsService =
        retrofitContainer.tripDrawRetrofit.create(GetAllTripsService::class.java)
    val deleteTripService: DeleteTripService =
        retrofitContainer.tripDrawRetrofit.create(DeleteTripService::class.java)
}
