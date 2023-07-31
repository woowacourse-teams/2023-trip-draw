package com.teamtripdraw.android

import android.app.Application
import com.teamtripdraw.android.di.LocalDataSourceContainer
import com.teamtripdraw.android.di.LocalPreferenceContainer
import com.teamtripdraw.android.di.RemoteDataSourceContainer
import com.teamtripdraw.android.di.RepositoryContainer
import com.teamtripdraw.android.di.RetrofitContainer
import com.teamtripdraw.android.di.ServiceContainer

class TripDrawApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initContainer()
    }

    private fun initContainer() {
        localPreferenceContainer = LocalPreferenceContainer(applicationContext)
        localDataSourceContainer = LocalDataSourceContainer(localPreferenceContainer)
        retrofitContainer =
            RetrofitContainer(localDataSourceContainer.localUserIdentifyInfoDataSource)
        serviceContainer = ServiceContainer(retrofitContainer)
        remoteDataSourceContainer = RemoteDataSourceContainer(serviceContainer, retrofitContainer)
        repositoryContainer =
            RepositoryContainer(localDataSourceContainer, remoteDataSourceContainer)
    }

    companion object DependencyContainer {
        lateinit var localPreferenceContainer: LocalPreferenceContainer
        lateinit var retrofitContainer: RetrofitContainer
        lateinit var serviceContainer: ServiceContainer
        lateinit var localDataSourceContainer: LocalDataSourceContainer
        lateinit var remoteDataSourceContainer: RemoteDataSourceContainer
        lateinit var repositoryContainer: RepositoryContainer
    }
}
