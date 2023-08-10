package com.teamtripdraw.android

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.kakao.sdk.common.KakaoSdk
import com.teamtripdraw.android.BuildConfig.KAKAO_NATIVE_APP_KEY
import com.teamtripdraw.android.di.LocalDataSourceContainer
import com.teamtripdraw.android.di.LocalPreferenceContainer
import com.teamtripdraw.android.di.RemoteDataSourceContainer
import com.teamtripdraw.android.di.RepositoryContainer
import com.teamtripdraw.android.di.RetrofitContainer
import com.teamtripdraw.android.di.ServiceContainer

class TripDrawApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initContainer()
        prohibitDarkMode()
        initKakaoSdk()
    }

    private fun initKakaoSdk() {
        KakaoSdk.init(this, KAKAO_NATIVE_APP_KEY)
    }

    private fun initContainer() {
        localPreferenceContainer = LocalPreferenceContainer(applicationContext)
        localDataSourceContainer = LocalDataSourceContainer(localPreferenceContainer)
        retrofitContainer =
            RetrofitContainer(localDataSourceContainer.localUserIdentifyInfoDataSource)
        serviceContainer = ServiceContainer(retrofitContainer)
        remoteDataSourceContainer = RemoteDataSourceContainer(serviceContainer, retrofitContainer)
        repositoryContainer =
            RepositoryContainer(localDataSourceContainer, remoteDataSourceContainer)
    }

    private fun prohibitDarkMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    companion object DependencyContainer {
        lateinit var localPreferenceContainer: LocalPreferenceContainer
        lateinit var retrofitContainer: RetrofitContainer
        lateinit var serviceContainer: ServiceContainer
        lateinit var localDataSourceContainer: LocalDataSourceContainer
        lateinit var remoteDataSourceContainer: RemoteDataSourceContainer
        lateinit var repositoryContainer: RepositoryContainer
    }
}
