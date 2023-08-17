package com.teamtripdraw.android.di

import com.teamtripdraw.android.data.dataSource.point.PointDataSource
import com.teamtripdraw.android.data.dataSource.point.RemotePointDataSourceImpl
import com.teamtripdraw.android.data.dataSource.post.PostDataSource
import com.teamtripdraw.android.data.dataSource.post.remote.RemotePostDataSourceImpl
import com.teamtripdraw.android.data.dataSource.signUp.RemoteSignUpDataSourceImpl
import com.teamtripdraw.android.data.dataSource.signUp.SignUpDataSource
import com.teamtripdraw.android.data.dataSource.trip.RemoteTripDataSourceImpl
import com.teamtripdraw.android.data.dataSource.trip.TripDataSource
import com.teamtripdraw.android.data.dataSource.unsubscribe.RemoteUnsubscribeDataSourceImpl
import com.teamtripdraw.android.data.dataSource.unsubscribe.UnsubscribeDataSource
import com.teamtripdraw.android.data.dataSource.userIdentifyInfo.RemoteUserIdentifyInfoDataSourceImpl
import com.teamtripdraw.android.data.dataSource.userIdentifyInfo.UserIdentifyInfoDataSource

class RemoteDataSourceContainer(
    serviceContainer: ServiceContainer,
    retrofitContainer: RetrofitContainer,
) {
    val remoteSignUpDataSource: SignUpDataSource.Remote =
        RemoteSignUpDataSourceImpl(
            serviceContainer.nicknameSetupService,
            serviceContainer.getNicknameService,
            retrofitContainer.tripDrawRetrofit,
        )
    val remoteTripDataSource: TripDataSource.Remote =
        RemoteTripDataSourceImpl(
            serviceContainer.createTripService,
            serviceContainer.getTripInfoService,
            serviceContainer.setTripTitleService,
            serviceContainer.getAllTripsService,
            serviceContainer.deleteTripService,
        )
    val remotePostDataSource: PostDataSource.Remote =
        RemotePostDataSourceImpl(
            retrofitContainer.moshi,
            serviceContainer.postService,
        )
    val remotePointDataSource: PointDataSource.Remote =
        RemotePointDataSourceImpl(
            serviceContainer.createRecordingPointService,
            serviceContainer.getPointService,
            serviceContainer.deletePointService,
        )
    val remoteUserIdentifyInfoDataSource: UserIdentifyInfoDataSource.Remote =
        RemoteUserIdentifyInfoDataSourceImpl(
            serviceContainer.loginService,
        )
    val unsubscribeDataSource: UnsubscribeDataSource.Remote =
        RemoteUnsubscribeDataSourceImpl(
            serviceContainer.unsubscribeService,
        )
}
