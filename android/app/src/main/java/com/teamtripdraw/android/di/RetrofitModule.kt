package com.teamtripdraw.android.di

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.teamtripdraw.android.BuildConfig
import com.teamtripdraw.android.data.dataSource.auth.userIdentifyInfo.UserIdentifyInfoDataSource
import com.teamtripdraw.android.data.httpClient.retrofitAdapter.responseState.ResponseStateCallAdapterFactory
import com.teamtripdraw.android.data.httpClient.retrofitAdapter.responseState.enqueueActions.ReLoginHandler
import com.teamtripdraw.android.data.httpClient.retrofitAdapter.responseState.enqueueActions.TripDrawEnqueueActions
import com.teamtripdraw.android.data.httpClient.retrofitAdapter.responseState.enqueueActions.TripDrawReLoginHandler
import com.teamtripdraw.android.data.httpClient.service.TokenRefreshService
import com.teamtripdraw.android.di.qualifier.GeneralOKHttpClient
import com.teamtripdraw.android.di.qualifier.NaverReverseGeocodingOkHttpClient
import com.teamtripdraw.android.di.qualifier.NaverReverseGeocodingRetrofit
import com.teamtripdraw.android.di.qualifier.TripDrawOkHttpClient
import com.teamtripdraw.android.di.qualifier.TripDrawRetrofit
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Dispatcher
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
    private const val AUTHORIZATION_INTERCEPTOR_VALUE_FORMAT = "Bearer %s"

    @Provides
    @TripDrawOkHttpClient
    fun providesAuthorizationInterceptor(userIdentifyInfoDataSource: UserIdentifyInfoDataSource.Local): Interceptor =
        Interceptor { chain ->
            with(chain) {
                proceed(
                    request()
                        .newBuilder()
                        .addHeader(
                            "Authorization",
                            AUTHORIZATION_INTERCEPTOR_VALUE_FORMAT.format(userIdentifyInfoDataSource.getAccessToken()),
                        )
                        .build(),
                )
            }
        }

    @Provides
    fun providesHttpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.HEADERS }

    @Provides
    fun providesGeneralOKHttpDispatcher(): Dispatcher = Dispatcher().apply {
        maxRequestsPerHost = 10
    }

    @Provides
    @GeneralOKHttpClient
    fun providesGeneralOKHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        generalOKHttpDispatcher: Dispatcher,
    ): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .dispatcher(generalOKHttpDispatcher)
            .build()

    // newBuilder 사용시 OkHttp의 기존 설정을 그대로 가져갈 수 있음 -> ConnectionPool,Dispatcher 등등 공유
    @Provides
    @TripDrawOkHttpClient
    fun providesTripDrawOkHttpClient(
        @GeneralOKHttpClient generalOKHttpClient: OkHttpClient,
        @TripDrawOkHttpClient authorizationInterceptor: Interceptor,
    ): OkHttpClient =
        generalOKHttpClient.newBuilder()
            .addInterceptor(authorizationInterceptor)
            .build()

    @Provides
    fun providesMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides
    @Singleton
    fun providesReLoginHandler(
        @ApplicationContext context: Context,
        userIdentifyInfoDataSource: UserIdentifyInfoDataSource.Local,
    ): ReLoginHandler =
        TripDrawReLoginHandler(context, userIdentifyInfoDataSource)

    @Provides
    @Singleton
    @TripDrawRetrofit
    fun providesTripDrawRetrofit(
        @TripDrawOkHttpClient tripDrawOkHttpClient: OkHttpClient,
        userIdentifyInfoDataSource: UserIdentifyInfoDataSource.Local,
        reLoginHandler: ReLoginHandler,
        moshi: Moshi,
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.TRIP_DRAW_BASE_URL)
            .client(tripDrawOkHttpClient)
            .addCallAdapterFactory(
                ResponseStateCallAdapterFactory(
                    TripDrawEnqueueActions::class,
                    TokenRefreshService::class.java,
                    userIdentifyInfoDataSource,
                    reLoginHandler,
                ),
            )
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

    @Provides
    @NaverReverseGeocodingOkHttpClient
    fun naverGeocodingOkHttpClient(
        @GeneralOKHttpClient generalOKHttpClient: OkHttpClient,
        @NaverReverseGeocodingOkHttpClient naverReverseGeocodingInterceptor: Interceptor,
    ): OkHttpClient =
        generalOKHttpClient.newBuilder()
            .addInterceptor(naverReverseGeocodingInterceptor)
            .build()

    @Provides
    @Singleton
    @NaverReverseGeocodingRetrofit
    fun providesNaverReverseGeocodingRetrofit(
        @NaverReverseGeocodingOkHttpClient naverReverseGeocodingOkHttpClient: OkHttpClient,
        moshi: Moshi,
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.NAVER_REVERS_GEOCODER_BASE_URL)
            .client(naverReverseGeocodingOkHttpClient)
            .addCallAdapterFactory(ResponseStateCallAdapterFactory())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

    @Provides
    @NaverReverseGeocodingOkHttpClient
    fun providesNaverReverseGeocodingInterceptor(): Interceptor = Interceptor { chain ->
        with(chain) {
            proceed(
                request()
                    .newBuilder()
                    .addHeader("X-NCP-APIGW-API-KEY-ID", BuildConfig.NAVER_MAP_CLIENT_ID)
                    .addHeader("X-NCP-APIGW-API-KEY", BuildConfig.NAVER_MAP_CLIENT_SECRET)
                    .build(),
            )
        }
    }
}
