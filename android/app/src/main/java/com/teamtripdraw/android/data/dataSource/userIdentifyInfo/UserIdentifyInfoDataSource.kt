package com.teamtripdraw.android.data.dataSource.userIdentifyInfo

import com.teamtripdraw.android.data.model.DataLoginInfo

interface UserIdentifyInfoDataSource {
    interface Local {
        fun setIdentifyInfo(identifyInfo: String)

        fun getIdentifyInfo(): String

        fun deleteIdentifyInfo()
    }

    interface Remote {
        suspend fun issueUserIdentifyInfo(dataLoginInfo: DataLoginInfo): Result<String>
    }
}
