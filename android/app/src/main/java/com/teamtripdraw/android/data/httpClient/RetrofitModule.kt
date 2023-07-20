package com.teamtripdraw.android.data.httpClient

import com.squareup.moshi.Moshi
import com.teamtripdraw.android.BuildConfig.TRIP_DRAW_BASE_URL
import com.teamtripdraw.android.data.httpClient.retrofitAdapter.ResponseStateCallAdapterFactory
import com.teamtripdraw.android.data.httpClient.service.SetNickNameService
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

object RetrofitModule {
//    로그인 작업시 유저 토큰 붙여주는 역할
//    private val authorizationInterceptor: Interceptor =
//        Interceptor { chain ->
//            with(chain) {
//                proceed(
//                    request()
//                        .newBuilder()
//                        .addHeader("Authorization","유저 토큰 삽입")
//                        .build()
//                )
//            }
//        }

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
            .addInterceptor(httpLoggingInterceptor)
            .dispatcher(tripDrawDispatcher)
            .build()

    private val moshi = Moshi.Builder().build()

    private val tripDrawRetrofit: Retrofit =
        Retrofit.Builder()
            .baseUrl(TRIP_DRAW_BASE_URL)
            .client(tripDrawOkHttpClient)
            .addCallAdapterFactory(ResponseStateCallAdapterFactory())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

    val setNickNameService: SetNickNameService =
        tripDrawRetrofit.create(SetNickNameService::class.java)
}
