package com.teamtripdraw.android.di

import com.teamtripdraw.android.data.repository.AuthRepositoryImpl
import com.teamtripdraw.android.data.repository.PointRepositoryImpl
import com.teamtripdraw.android.data.repository.PostRepositoryImpl
import com.teamtripdraw.android.data.repository.TripRepositoryImpl
import com.teamtripdraw.android.domain.repository.AuthRepository
import com.teamtripdraw.android.domain.repository.PointRepository
import com.teamtripdraw.android.domain.repository.PostRepository
import com.teamtripdraw.android.domain.repository.TripRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindsAuthRepository(repository: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    fun bindsTripRepository(repository: TripRepositoryImpl): TripRepository

    @Binds
    @Singleton
    fun bindsPostRepository(repository: PostRepositoryImpl): PostRepository

    @Binds
    @Singleton
    fun bindsPointRepository(repository: PointRepositoryImpl): PointRepository
}
