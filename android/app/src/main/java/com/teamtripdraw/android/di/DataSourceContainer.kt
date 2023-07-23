package com.teamtripdraw.android.di

import com.teamtripdraw.android.data.dataSource.nicknameSetup.NicknameSetupDataSource
import com.teamtripdraw.android.data.dataSource.nicknameSetup.remote.RemoteNicknameSetupDataSourceImpl
import com.teamtripdraw.android.data.dataSource.userIdentifyInfo.UserIdentifyInfoDataSource
import com.teamtripdraw.android.data.dataSource.userIdentifyInfo.local.LocalUserIdentifyInfoDataSourceImpl

class DataSourceContainer(
    serviceContainer: ServiceContainer,
    localPreferenceContainer: LocalPreferenceContainer
) {
    // remote
    val remoteNicknameSetupDataSource: NicknameSetupDataSource.Remote =
        RemoteNicknameSetupDataSourceImpl(serviceContainer.nicknameSetupService)

    // local
    val userIdentifyInfoDataSource: UserIdentifyInfoDataSource.Local =
        LocalUserIdentifyInfoDataSourceImpl(localPreferenceContainer.userIdentifyInfoPreference)
}
