package com.teamtripdraw.android.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.teamtripdraw.android.BuildConfig
import com.teamtripdraw.android.data.dataSource.userIdentifyInfo.UserIdentifyInfoDataSource
import com.teamtripdraw.android.data.httpClient.retrofitAdapter.ResponseStateCallAdapterFactory
import okhttp3.Dispatcher
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitContainer(userIdentifyInfoDataSource: UserIdentifyInfoDataSource.Local) {
    private val authorizationInterceptor: Interceptor =
        Interceptor { chain ->
            with(chain) {
                proceed(
                    request()
                        .newBuilder()
                        .addHeader(
                            "Basic",
                            AUTHORIZATION_INTERCEPTOR_VALUE_FORMAT.format(userIdentifyInfoDataSource.getIdentifyInfo())
                        )
                        .build()
                )
            }
        }

    private val httpLoggingInterceptor: HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.HEADERS }

    private val tripDrawDispatcher = Dispatcher().apply {
        maxRequestsPerHost = 10
    }

    private val tripDrawOkHttpClient: OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(authorizationInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .dispatcher(tripDrawDispatcher)
            .build()

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val tripDrawRetrofit: Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.TRIP_DRAW_BASE_URL)
            .client(tripDrawOkHttpClient)
            .addCallAdapterFactory(ResponseStateCallAdapterFactory())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

    companion object {
        private const val AUTHORIZATION_INTERCEPTOR_VALUE_FORMAT = "basic %s"
    }
}
