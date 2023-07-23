package com.teamtripdraw.android.data.dataSource.userIdentifyInfo.local

import android.content.SharedPreferences
import androidx.core.content.edit
import com.teamtripdraw.android.data.dataSource.userIdentifyInfo.UserIdentifyInfoDataSource
import java.util.Base64

class LocalUserIdentifyInfoDataSourceImpl(private val userIdentifyInfoPreference: SharedPreferences) :
    UserIdentifyInfoDataSource.Local {

    override fun setIdentifyInfo(identifyInfo: String) {
        val encodedNickNameByBase64 =
            Base64.getEncoder().encodeToString(identifyInfo.toByteArray(Charsets.UTF_8))
        userIdentifyInfoPreference.edit {
            putString(
                TRIP_DRAW_IDENTIFY_INFO,
                encodedNickNameByBase64
            )
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
