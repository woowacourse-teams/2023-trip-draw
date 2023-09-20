package com.teamtripdraw.android.di

import com.teamtripdraw.android.data.dataSource.auth.login.LoginDataSource
import com.teamtripdraw.android.data.dataSource.auth.login.RemoteLoginDataSourceImpl
import com.teamtripdraw.android.data.dataSource.auth.signUp.RemoteSignUpDataSourceImpl
import com.teamtripdraw.android.data.dataSource.auth.signUp.SignUpDataSource
import com.teamtripdraw.android.data.dataSource.point.PointDataSource
import com.teamtripdraw.android.data.dataSource.point.RemotePointDataSourceImpl
import com.teamtripdraw.android.data.dataSource.post.PostDataSource
import com.teamtripdraw.android.data.dataSource.post.remote.RemotePostDataSourceImpl
import com.teamtripdraw.android.data.dataSource.trip.RemoteTripDataSourceImpl
import com.teamtripdraw.android.data.dataSource.trip.TripDataSource
import com.teamtripdraw.android.data.dataSource.unsubscribe.RemoteUnsubscribeDataSourceImpl
import com.teamtripdraw.android.data.dataSource.unsubscribe.UnsubscribeDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RemoteDataSourceModule {
    @Binds
    @Singleton
    fun bindsRemoteSignUpDataSource(source: RemoteSignUpDataSourceImpl): SignUpDataSource.Remote

    @Binds
    @Singleton
    fun bindsRemoteTripDataSource(source: RemoteTripDataSourceImpl): TripDataSource.Remote

    @Binds
    @Singleton
    fun bindsRemotePostDataSource(source: RemotePostDataSourceImpl): PostDataSource.Remote

    @Binds
    @Singleton
    fun bindsRemotePointDataSource(source: RemotePointDataSourceImpl): PointDataSource.Remote

    @Binds
    @Singleton
    fun bindsRemoteUnsubscribeDataSource(source: RemoteUnsubscribeDataSourceImpl): UnsubscribeDataSource.Remote

    @Binds
    @Singleton
    fun bindsRemoteLoginDataSource(source: RemoteLoginDataSourceImpl): LoginDataSource.Remote
}
