package com.teamtripdraw.android.di

import com.teamtripdraw.android.data.dataSource.auth.login.LoginDataSource
import com.teamtripdraw.android.data.dataSource.auth.login.RemoteLoginDataSourceImpl
import com.teamtripdraw.android.data.dataSource.auth.signUp.RemoteSignUpDataSourceImpl
import com.teamtripdraw.android.data.dataSource.auth.signUp.SignUpDataSource
import com.teamtripdraw.android.data.dataSource.auth.userIdentifyInfo.UserIdentifyInfoDataSource
import com.teamtripdraw.android.data.dataSource.point.PointDataSource
import com.teamtripdraw.android.data.dataSource.point.RemotePointDataSourceImpl
import com.teamtripdraw.android.data.dataSource.post.PostDataSource
import com.teamtripdraw.android.data.dataSource.post.remote.RemotePostDataSourceImpl
import com.teamtripdraw.android.data.dataSource.trip.RemoteTripDataSourceImpl
import com.teamtripdraw.android.data.dataSource.trip.TripDataSource
import com.teamtripdraw.android.data.dataSource.unsubscribe.RemoteUnsubscribeDataSourceImpl
import com.teamtripdraw.android.data.dataSource.unsubscribe.UnsubscribeDataSource

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
    val remoteUnsubscribeDataSource: UnsubscribeDataSource.Remote =
        RemoteUnsubscribeDataSourceImpl(
            serviceContainer.unsubscribeService,
        )
    val remoteLoginDataSource: LoginDataSource.Remote =
        RemoteLoginDataSourceImpl(
            serviceContainer.loginService,
        )
}
