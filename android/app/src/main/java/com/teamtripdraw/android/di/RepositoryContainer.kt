package com.teamtripdraw.android.di

import com.teamtripdraw.android.data.repository.NicknameSetupRepositoryImpl
import com.teamtripdraw.android.data.repository.PointRepositoryImpl
import com.teamtripdraw.android.data.repository.PostRepositoryImpl
import com.teamtripdraw.android.data.repository.TripRepositoryImpl
import com.teamtripdraw.android.domain.repository.NicknameSetupRepository
import com.teamtripdraw.android.domain.repository.PointRepository
import com.teamtripdraw.android.domain.repository.PostRepository
import com.teamtripdraw.android.domain.repository.TripRepository

class RepositoryContainer(
    localDataSourceContainer: LocalDataSourceContainer,
    remoteDataSourceContainer: RemoteDataSourceContainer
) {
    val nicknameSetupRepository: NicknameSetupRepository = NicknameSetupRepositoryImpl(
        localDataSourceContainer.localUserIdentifyInfoDataSource,
        remoteDataSourceContainer.remoteNicknameSetupDataSource
    )
    val tripRepository: TripRepository = TripRepositoryImpl(
        remoteDataSourceContainer.remoteTripDataSource,
        localDataSourceContainer.localTripDataSource
    )
    val postRepository: PostRepository = PostRepositoryImpl(
        remoteDataSourceContainer.remotePostDataSource
    )
    val pointRepository: PointRepository = PointRepositoryImpl(
        remoteDataSourceContainer.remotePointDataSource,
        localDataSourceContainer.localTripDataSource
    )
}
