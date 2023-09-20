package com.teamtripdraw.android.data.dataSource.auth.userIdentifyInfo

import android.content.SharedPreferences
import androidx.core.content.edit
import com.teamtripdraw.android.data.model.DataUserIdentifyInfo
import javax.inject.Inject

class LocalUserIdentifyInfoDataSourceImpl @Inject constructor(private val userIdentifyInfoPreference: SharedPreferences) :
    UserIdentifyInfoDataSource.Local {

    override fun setIdentifyInfo(identifyInfo: DataUserIdentifyInfo) {
        userIdentifyInfoPreference.edit {
            putString(TRIP_DRAW_ACCESS_TOKEN, identifyInfo.accessToken)
            putString(TRIP_DRAW_REFRESH_TOKEN, identifyInfo.refreshToken)
        }
    }

    override fun getAccessToken(): String =
        userIdentifyInfoPreference.getString(TRIP_DRAW_ACCESS_TOKEN, "") ?: ""

    override fun getRefreshToken(): String =
        userIdentifyInfoPreference.getString(TRIP_DRAW_REFRESH_TOKEN, "") ?: ""

    override fun deleteIdentifyInfo() {
        userIdentifyInfoPreference.edit {
            remove(TRIP_DRAW_ACCESS_TOKEN)
            remove(TRIP_DRAW_REFRESH_TOKEN)
        }
    }

    companion object UserIdentifyInfoKey {
        private const val TRIP_DRAW_ACCESS_TOKEN = "TRIP_DRAW_ACCESS_TOKEN"
        private const val TRIP_DRAW_REFRESH_TOKEN = "TRIP_DRAW_REFRESH_TOKEN"
    }
}
