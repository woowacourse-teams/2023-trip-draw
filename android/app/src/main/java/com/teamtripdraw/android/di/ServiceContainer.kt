package com.teamtripdraw.android.di

import com.teamtripdraw.android.data.httpClient.service.CreateRecordingPointService
import com.teamtripdraw.android.data.httpClient.service.CreateTripService
import com.teamtripdraw.android.data.httpClient.service.DeletePointService
import com.teamtripdraw.android.data.httpClient.service.DeleteTripService
import com.teamtripdraw.android.data.httpClient.service.GetAllTripsService
import com.teamtripdraw.android.data.httpClient.service.GetPointService
import com.teamtripdraw.android.data.httpClient.service.GetTripInfoService
import com.teamtripdraw.android.data.httpClient.service.GetUserInfoService
import com.teamtripdraw.android.data.httpClient.service.LoginService
import com.teamtripdraw.android.data.httpClient.service.NaverGeocodingService
import com.teamtripdraw.android.data.httpClient.service.NicknameSetupService
import com.teamtripdraw.android.data.httpClient.service.PostService
import com.teamtripdraw.android.data.httpClient.service.SetTripTitleService
import com.teamtripdraw.android.data.httpClient.service.UnsubscribeService

class ServiceContainer(retrofitContainer: RetrofitContainer) {
    val nicknameSetupService: NicknameSetupService =
        retrofitContainer.tripDrawRetrofit.create(NicknameSetupService::class.java)
    val getNicknameService: GetUserInfoService =
        retrofitContainer.tripDrawRetrofit.create(GetUserInfoService::class.java)
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
    val loginService: LoginService =
        retrofitContainer.tripDrawRetrofit.create(LoginService::class.java)
    val unsubscribeService: UnsubscribeService =
        retrofitContainer.tripDrawRetrofit.create(UnsubscribeService::class.java)
    val naverGeocodingService: NaverGeocodingService =
        retrofitContainer.naverReverseGeocodingRetrofit.create(NaverGeocodingService::class.java)
}
