package com.teamtripdraw.android.di

import com.teamtripdraw.android.data.dataSource.nicknameSetup.NicknameSetupDataSource
import com.teamtripdraw.android.data.dataSource.nicknameSetup.RemoteNicknameSetupDataSourceImpl
import com.teamtripdraw.android.data.dataSource.point.PointDataSource
import com.teamtripdraw.android.data.dataSource.point.RemotePointDataSourceImpl
import com.teamtripdraw.android.data.dataSource.post.PostDataSource
import com.teamtripdraw.android.data.dataSource.post.remote.RemotePostDataSourceImpl
import com.teamtripdraw.android.data.dataSource.trip.RemoteTripDataSourceImpl
import com.teamtripdraw.android.data.dataSource.trip.TripDataSource

class RemoteDataSourceContainer(
    serviceContainer: ServiceContainer,
    retrofitContainer: RetrofitContainer
) {
    val remoteNicknameSetupDataSource: NicknameSetupDataSource.Remote =
        RemoteNicknameSetupDataSourceImpl(
            serviceContainer.nicknameSetupService,
            serviceContainer.getNicknameService,
            retrofitContainer.tripDrawRetrofit
        )
    val remoteTripDataSource: TripDataSource.Remote =
        RemoteTripDataSourceImpl(
            serviceContainer.createTripService,
            serviceContainer.getTripInfoService
        )
    val remotePostDataSource: PostDataSource.Remote =
        RemotePostDataSourceImpl(serviceContainer.postService)
    val remotePointDataSource: PointDataSource.Remote =
        RemotePointDataSourceImpl(serviceContainer.createRecordingPointService)
}
