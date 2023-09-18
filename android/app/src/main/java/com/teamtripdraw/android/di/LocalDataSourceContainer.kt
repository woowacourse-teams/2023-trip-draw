package com.teamtripdraw.android.di

import com.teamtripdraw.android.data.dataSource.trip.LocalTripDataSourceImpl
import com.teamtripdraw.android.data.dataSource.trip.TripDataSource
import com.teamtripdraw.android.data.dataSource.auth.userIdentifyInfo.LocalUserIdentifyInfoDataSourceImpl
import com.teamtripdraw.android.data.dataSource.auth.userIdentifyInfo.UserIdentifyInfoDataSource

class LocalDataSourceContainer(localPreferenceContainer: LocalPreferenceContainer) {
    val localUserIdentifyInfoDataSource: UserIdentifyInfoDataSource.Local =
        LocalUserIdentifyInfoDataSourceImpl(localPreferenceContainer.userIdentifyInfoPreference)
    val localTripDataSource: TripDataSource.Local =
        LocalTripDataSourceImpl(localPreferenceContainer.tripPreference)
}
