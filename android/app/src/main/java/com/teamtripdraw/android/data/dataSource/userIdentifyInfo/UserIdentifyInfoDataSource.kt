package com.teamtripdraw.android.data.dataSource.userIdentifyInfo

interface UserIdentifyInfoDataSource {
    interface Local {
        fun setIdentifyInfo(identifyInfo: String)

        fun getIdentifyInfo(): String

        fun deleteIdentifyInfo()
    }

    interface Remote
}
