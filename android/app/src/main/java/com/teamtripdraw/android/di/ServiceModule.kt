package com.teamtripdraw.android.di

import com.teamtripdraw.android.data.httpClient.service.CreateCurrentPointPostService
import com.teamtripdraw.android.data.httpClient.service.CreateRecordingPointService
import com.teamtripdraw.android.data.httpClient.service.CreateTripService
import com.teamtripdraw.android.data.httpClient.service.DeletePointService
import com.teamtripdraw.android.data.httpClient.service.DeleteTripService
import com.teamtripdraw.android.data.httpClient.service.GetAddressesService
import com.teamtripdraw.android.data.httpClient.service.GetAllAddressesService
import com.teamtripdraw.android.data.httpClient.service.GetAllTripsService
import com.teamtripdraw.android.data.httpClient.service.GetPointPostService
import com.teamtripdraw.android.data.httpClient.service.GetPointService
import com.teamtripdraw.android.data.httpClient.service.GetTripInfoService
import com.teamtripdraw.android.data.httpClient.service.GetUserInfoService
import com.teamtripdraw.android.data.httpClient.service.LoginService
import com.teamtripdraw.android.data.httpClient.service.NaverGeocodingService
import com.teamtripdraw.android.data.httpClient.service.NicknameSetupService
import com.teamtripdraw.android.data.httpClient.service.PostService
import com.teamtripdraw.android.data.httpClient.service.SetTripTitleService
import com.teamtripdraw.android.data.httpClient.service.UnsubscribeService
import com.teamtripdraw.android.di.qualifier.NaverReverseGeocodingRetrofit
import com.teamtripdraw.android.di.qualifier.TripDrawRetrofit
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
    @Provides
    @Singleton
    fun providesNicknameSetupService(@TripDrawRetrofit retrofit: Retrofit): NicknameSetupService =
        retrofit.create(NicknameSetupService::class.java)

    @Provides
    @Singleton
    fun providesGetNicknameService(@TripDrawRetrofit retrofit: Retrofit): GetUserInfoService =
        retrofit.create(GetUserInfoService::class.java)

    @Provides
    @Singleton
    fun providesCreateTripService(@TripDrawRetrofit retrofit: Retrofit): CreateTripService =
        retrofit.create(CreateTripService::class.java)

    @Provides
    @Singleton
    fun providesPostService(@TripDrawRetrofit retrofit: Retrofit): PostService =
        retrofit.create(PostService::class.java)

    @Provides
    @Singleton
    fun providesCreateCurrentPointPostService(@TripDrawRetrofit retrofit: Retrofit): CreateCurrentPointPostService =
        retrofit.create(CreateCurrentPointPostService::class.java)

    @Provides
    @Singleton
    fun providesCreateRecordingPointService(@TripDrawRetrofit retrofit: Retrofit): CreateRecordingPointService =
        retrofit.create(CreateRecordingPointService::class.java)

    @Provides
    @Singleton
    fun providesGetPointPostService(@TripDrawRetrofit retrofit: Retrofit): GetPointPostService =
        retrofit.create(GetPointPostService::class.java)

    @Provides
    @Singleton
    fun providesGetTripInfoService(@TripDrawRetrofit retrofit: Retrofit): GetTripInfoService =
        retrofit.create(GetTripInfoService::class.java)

    @Provides
    @Singleton
    fun providesGetPointService(@TripDrawRetrofit retrofit: Retrofit): GetPointService =
        retrofit.create(GetPointService::class.java)

    @Provides
    @Singleton
    fun providesDeletePointService(@TripDrawRetrofit retrofit: Retrofit): DeletePointService =
        retrofit.create(DeletePointService::class.java)

    @Provides
    @Singleton
    fun providesSetTripTitleService(@TripDrawRetrofit retrofit: Retrofit): SetTripTitleService =
        retrofit.create(SetTripTitleService::class.java)

    @Provides
    @Singleton
    fun providesGetAllTripsService(@TripDrawRetrofit retrofit: Retrofit): GetAllTripsService =
        retrofit.create(GetAllTripsService::class.java)

    @Provides
    @Singleton
    fun providesDeleteTripService(@TripDrawRetrofit retrofit: Retrofit): DeleteTripService =
        retrofit.create(DeleteTripService::class.java)

    @Provides
    @Singleton
    fun providesLoginService(@TripDrawRetrofit retrofit: Retrofit): LoginService =
        retrofit.create(LoginService::class.java)

    @Provides
    @Singleton
    fun providesUnsubscribeService(@TripDrawRetrofit retrofit: Retrofit): UnsubscribeService =
        retrofit.create(UnsubscribeService::class.java)

    @Provides
    @Singleton
    fun providesGetAddressesService(@TripDrawRetrofit retrofit: Retrofit): GetAddressesService =
        retrofit.create(GetAddressesService::class.java)

    @Provides
    @Singleton
    fun providersGetAllAddressesService(@TripDrawRetrofit retrofit: Retrofit): GetAllAddressesService =
        retrofit.create(GetAllAddressesService::class.java)

    @Provides
    @Singleton
    fun providesNaverReverseGeocodingService(@NaverReverseGeocodingRetrofit retrofit: Retrofit): NaverGeocodingService =
        retrofit.create(NaverGeocodingService::class.java)
}
