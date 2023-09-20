package com.teamtripdraw.android.di

import com.teamtripdraw.android.data.repository.AuthRepositoryImpl
import com.teamtripdraw.android.data.repository.GeocodingRepositoryImpl
import com.teamtripdraw.android.data.repository.PointRepositoryImpl
import com.teamtripdraw.android.data.repository.PostRepositoryImpl
import com.teamtripdraw.android.data.repository.TripRepositoryImpl
import com.teamtripdraw.android.domain.repository.AuthRepository
import com.teamtripdraw.android.domain.repository.GeocodingRepository
import com.teamtripdraw.android.domain.repository.PointRepository
import com.teamtripdraw.android.domain.repository.PostRepository
import com.teamtripdraw.android.domain.repository.TripRepository

class RepositoryContainer(
    localDataSourceContainer: LocalDataSourceContainer,
    remoteDataSourceContainer: RemoteDataSourceContainer,
) {
    val authRepository: AuthRepository = AuthRepositoryImpl(
        localDataSourceContainer.localUserIdentifyInfoDataSource,
        remoteDataSourceContainer.remoteSignUpDataSource,
        remoteDataSourceContainer.remoteLoginDataSource,
        remoteDataSourceContainer.remoteUnsubscribeDataSource,
    )
    val tripRepository: TripRepository = TripRepositoryImpl(
        remoteDataSourceContainer.remoteTripDataSource,
        localDataSourceContainer.localTripDataSource,
    )
    val postRepository: PostRepository = PostRepositoryImpl(
        remoteDataSourceContainer.remotePostDataSource,
    )
    val pointRepository: PointRepository = PointRepositoryImpl(
        remoteDataSourceContainer.remotePointDataSource,
        localDataSourceContainer.localTripDataSource,
    )
    val geocodingRepository: GeocodingRepository = GeocodingRepositoryImpl(
        remoteDataSourceContainer.remoteGeocodingDataSource,
    )
}
