package com.teamtripdraw.android.di

import com.teamtripdraw.android.data.repository.NicknameSetupRepositoryImpl
import com.teamtripdraw.android.data.repository.TripRepositoryImpl
import com.teamtripdraw.android.domain.repository.NicknameSetupRepository
import com.teamtripdraw.android.domain.repository.TripRepository

class RepositoryContainer(
    localDataSourceContainer: LocalDataSourceContainer,
    remoteDataSourceContainer: RemoteDataSourceContainer,
) {
    val nicknameSetupRepository: NicknameSetupRepository = NicknameSetupRepositoryImpl(
        localDataSourceContainer.localUserIdentifyInfoDataSource,
        remoteDataSourceContainer.remoteNicknameSetupDataSource
    )
    val tripRepository: TripRepository = TripRepositoryImpl(
        remoteDataSourceContainer.remoteTripDataSource,
        localDataSourceContainer.localTripDataSource
    )
}
