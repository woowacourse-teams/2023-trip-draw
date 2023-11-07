package com.teamtripdraw.android.di

import com.teamtripdraw.android.data.dataSource.address.AddressDataSource
import com.teamtripdraw.android.data.dataSource.address.LocalAddressDataSourceImpl
import com.teamtripdraw.android.data.dataSource.auth.userIdentifyInfo.LocalUserIdentifyInfoDataSourceImpl
import com.teamtripdraw.android.data.dataSource.auth.userIdentifyInfo.UserIdentifyInfoDataSource
import com.teamtripdraw.android.data.dataSource.trip.LocalTripDataSourceImpl
import com.teamtripdraw.android.data.dataSource.trip.TripDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface LocalDataSourceModule {

    @Binds
    @Singleton
    fun bindsLocalUserIdentifyInfoDataSource(source: LocalUserIdentifyInfoDataSourceImpl): UserIdentifyInfoDataSource.Local

    @Binds
    @Singleton
    fun bindsLocalTripDataSource(source: LocalTripDataSourceImpl): TripDataSource.Local

    @Binds
    @Singleton
    fun bindsLocalAddressDataSource(source: LocalAddressDataSourceImpl): AddressDataSource.Local
}
