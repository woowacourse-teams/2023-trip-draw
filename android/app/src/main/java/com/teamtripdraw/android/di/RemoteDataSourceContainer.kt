package com.teamtripdraw.android.di

import com.teamtripdraw.android.data.dataSource.createTrip.CreateTripDataSource
import com.teamtripdraw.android.data.dataSource.createTrip.RemoteCreateTripDataSourceImpl
import com.teamtripdraw.android.data.dataSource.nicknameSetup.NicknameSetupDataSource
import com.teamtripdraw.android.data.dataSource.nicknameSetup.RemoteNicknameSetupDataSourceImpl

class RemoteDataSourceContainer(
    serviceContainer: ServiceContainer
) {
    val remoteNicknameSetupDataSource: NicknameSetupDataSource.Remote =
        RemoteNicknameSetupDataSourceImpl(serviceContainer.nicknameSetupService)
    val remoteCreateTripDataSourceImpl: CreateTripDataSource.Remote =
        RemoteCreateTripDataSourceImpl(serviceContainer.createTripService)
}
