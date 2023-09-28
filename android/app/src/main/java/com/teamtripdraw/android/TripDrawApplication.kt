package com.teamtripdraw.android

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.kakao.sdk.common.KakaoSdk
import com.teamtripdraw.android.BuildConfig.KAKAO_NATIVE_APP_KEY
import com.teamtripdraw.android.support.framework.presentation.log.LogUtil
import com.teamtripdraw.android.support.framework.presentation.log.tripDrawLogUtil.TripDrawLogUtil
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class TripDrawApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        prohibitDarkMode()
        initKakaoSdk()
        initTimber()
        initLogUtil()
    }

    private fun initKakaoSdk() {
        KakaoSdk.init(this, KAKAO_NATIVE_APP_KEY)
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
        lateinit var logUtil: LogUtil
    }
}
