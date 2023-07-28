package com.teamtripdraw.android.di

import com.teamtripdraw.android.data.dataSource.createTrip.TripDataSource
import com.teamtripdraw.android.data.dataSource.createTrip.RemoteTripDataSourceImpl
import com.teamtripdraw.android.data.dataSource.nicknameSetup.NicknameSetupDataSource
import com.teamtripdraw.android.data.dataSource.nicknameSetup.RemoteNicknameSetupDataSourceImpl

class RemoteDataSourceContainer(
    serviceContainer: ServiceContainer
) {
    val remoteNicknameSetupDataSource: NicknameSetupDataSource.Remote =
        RemoteNicknameSetupDataSourceImpl(serviceContainer.nicknameSetupService)
    val remoteTripDataSource: TripDataSource.Remote =
        RemoteTripDataSourceImpl(serviceContainer.createTripService)
}
