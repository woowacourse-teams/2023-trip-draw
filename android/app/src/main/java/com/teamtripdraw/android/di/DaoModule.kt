package com.teamtripdraw.android.di

import android.content.Context
import com.teamtripdraw.android.data.localDatabase.TripDrawDatabase
import com.teamtripdraw.android.data.localDatabase.dao.AddressDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DaoModule {

    @Provides
    @Singleton
    fun bindsAddressDao(@ApplicationContext context: Context): AddressDao =
        TripDrawDatabase.getDatabase(context).addressDao()
}
