package com.teamtripdraw.android.di

import com.teamtripdraw.android.data.dataSource.setNickName.SetNickNameDataSource
import com.teamtripdraw.android.data.dataSource.setNickName.remote.RemoteSetNickNameDataSourceImpl

class DataSourceContainer(serviceContainer: ServiceContainer) {
    // remote
    val remoteSetNickNameDataSource: SetNickNameDataSource.Remote =
        RemoteSetNickNameDataSourceImpl(serviceContainer.setNickNameService)

    // local
}
