package com.teamtripdraw.android.data.dataSource.auth.userIdentifyInfo

import com.teamtripdraw.android.data.model.DataUserIdentifyInfo

interface UserIdentifyInfoDataSource {
    interface Local {
        fun setIdentifyInfo(identifyInfo: DataUserIdentifyInfo)

        fun getAccessToken(): String

        fun getRefreshToken(): String

        fun deleteIdentifyInfo()
    }
}
