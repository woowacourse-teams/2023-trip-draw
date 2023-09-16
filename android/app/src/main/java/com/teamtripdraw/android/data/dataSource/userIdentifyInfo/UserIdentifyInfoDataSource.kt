package com.teamtripdraw.android.data.dataSource.userIdentifyInfo

import com.teamtripdraw.android.data.model.DataLoginInfo
import com.teamtripdraw.android.data.model.DataUserIdentifyInfo

interface UserIdentifyInfoDataSource {
    interface Local {
        fun setIdentifyInfo(identifyInfo: DataUserIdentifyInfo)

        fun getAccessToken(): String

        fun getRefreshToken(): String

        fun deleteIdentifyInfo()
    }

    interface Remote {
        suspend fun issueUserIdentifyInfo(dataLoginInfo: DataLoginInfo): Result<DataUserIdentifyInfo>
    }
}
