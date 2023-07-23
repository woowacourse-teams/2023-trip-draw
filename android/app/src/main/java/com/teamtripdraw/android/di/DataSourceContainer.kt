package com.teamtripdraw.android.di

import com.teamtripdraw.android.data.dataSource.nicknameSetup.NicknameSetupDataSource
import com.teamtripdraw.android.data.dataSource.nicknameSetup.remote.RemoteNicknameSetupDataSourceImpl

class DataSourceContainer(serviceContainer: ServiceContainer) {
    // remote
    val remoteNicknameSetupDataSource: NicknameSetupDataSource.Remote =
        RemoteNicknameSetupDataSourceImpl(serviceContainer.nicknameSetupService)

    // local
}
