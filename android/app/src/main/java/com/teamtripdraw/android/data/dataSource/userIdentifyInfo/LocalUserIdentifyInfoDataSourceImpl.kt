package com.teamtripdraw.android.data.dataSource.userIdentifyInfo

import android.content.SharedPreferences
import androidx.core.content.edit

class LocalUserIdentifyInfoDataSourceImpl(private val userIdentifyInfoPreference: SharedPreferences) :
    UserIdentifyInfoDataSource.Local {

    override fun setIdentifyInfo(identifyInfo: String) {
        userIdentifyInfoPreference.edit {
            putString(TRIP_DRAW_IDENTIFY_INFO, identifyInfo)
        }
    }

    override fun getIdentifyInfo(): String =
        userIdentifyInfoPreference.getString(TRIP_DRAW_IDENTIFY_INFO, "") ?: ""

    override fun deleteIdentifyInfo() {
        userIdentifyInfoPreference.edit { remove(TRIP_DRAW_IDENTIFY_INFO) }
    }

    companion object UserIdentifyInfoKey {
        private const val TRIP_DRAW_IDENTIFY_INFO = "TRIP_DRAW_IDENTIFY_INFO"
    }
}
