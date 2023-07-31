package com.teamtripdraw.android.di

import com.teamtripdraw.android.data.dataSource.trip.TripDataSource
import com.teamtripdraw.android.data.dataSource.trip.RemoteTripDataSourceImpl
import com.teamtripdraw.android.data.dataSource.nicknameSetup.NicknameSetupDataSource
import com.teamtripdraw.android.data.dataSource.nicknameSetup.RemoteNicknameSetupDataSourceImpl

class RemoteDataSourceContainer(
    serviceContainer: ServiceContainer
) {
    val remoteNicknameSetupDataSource: NicknameSetupDataSource.Remote =
        RemoteNicknameSetupDataSourceImpl(
            serviceContainer.nicknameSetupService,
            serviceContainer.getNicknameService
        )
    val remoteTripDataSource: TripDataSource.Remote =
        RemoteTripDataSourceImpl(serviceContainer.createTripService)
}
