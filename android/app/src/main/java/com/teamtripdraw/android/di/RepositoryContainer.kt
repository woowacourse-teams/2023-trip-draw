package com.teamtripdraw.android.di

import com.teamtripdraw.android.data.repository.TripRepositoryImpl
import com.teamtripdraw.android.data.repository.NicknameSetupRepositoryImpl
import com.teamtripdraw.android.domain.repository.TripRepository
import com.teamtripdraw.android.domain.repository.NicknameSetupRepository

class RepositoryContainer(
    localDataSourceContainer: LocalDataSourceContainer,
    remoteDataSourceContainer: RemoteDataSourceContainer,
    retrofitContainer: RetrofitContainer
) {
    val nicknameSetupRepository: NicknameSetupRepository = NicknameSetupRepositoryImpl(
        localDataSourceContainer.userIdentifyInfoDataSource,
        remoteDataSourceContainer.remoteNicknameSetupDataSource,
        retrofitContainer.tripDrawRetrofit
    )
    val tripRepository: TripRepository = TripRepositoryImpl(
        remoteDataSourceContainer.remoteTripDataSource
    )
}
