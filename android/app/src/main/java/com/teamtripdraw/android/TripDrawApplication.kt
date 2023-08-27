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
import com.teamtripdraw.android.support.framework.presentation.log.LogUtil
import com.teamtripdraw.android.support.framework.presentation.log.tripDrawLogUtil.TripDrawLogUtil
import timber.log.Timber

class TripDrawApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initContainer()
        prohibitDarkMode()
        initKakaoSdk()
        initTimber()
        initLogUtil()
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

    private fun initTimber() {
        if (!BuildConfig.IS_RELEASE) Timber.plant(getTripDrawDebugTree())
    }

    private fun getTripDrawDebugTree() = object : Timber.DebugTree() {
        override fun createStackElementTag(element: StackTraceElement): String? {
            return "${element.fileName}:${element.lineNumber}"
        }
    }

    private fun initLogUtil() {
        logUtil = TripDrawLogUtil()
    }

    companion object DependencyContainer {
        lateinit var localPreferenceContainer: LocalPreferenceContainer
        lateinit var retrofitContainer: RetrofitContainer
        lateinit var serviceContainer: ServiceContainer
        lateinit var localDataSourceContainer: LocalDataSourceContainer
        lateinit var remoteDataSourceContainer: RemoteDataSourceContainer
        lateinit var repositoryContainer: RepositoryContainer

        lateinit var logUtil: LogUtil
    }
}
