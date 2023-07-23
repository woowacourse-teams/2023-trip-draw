package com.teamtripdraw.android.di

import com.teamtripdraw.android.data.dataSource.userIdentifyInfo.UserIdentifyInfoDataSource
import com.teamtripdraw.android.data.dataSource.userIdentifyInfo.local.LocalUserIdentifyInfoDataSourceImpl

class LocalDataSourceContainer(localPreferenceContainer: LocalPreferenceContainer) {
    val userIdentifyInfoDataSource: UserIdentifyInfoDataSource.Local =
        LocalUserIdentifyInfoDataSourceImpl(localPreferenceContainer.userIdentifyInfoPreference)
}
