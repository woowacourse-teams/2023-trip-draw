package com.teamtripdraw.android

import android.app.Application
import com.teamtripdraw.android.di.DataSourceContainer
import com.teamtripdraw.android.di.RepositoryContainer
import com.teamtripdraw.android.di.RetrofitContainer
import com.teamtripdraw.android.di.ServiceContainer

class TripDrawApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initContainer()
    }

    private fun initContainer() {
        retrofitContainer = RetrofitContainer()
        serviceContainer = ServiceContainer(retrofitContainer)
        dataSourceContainer = DataSourceContainer(serviceContainer)
        repositoryContainer = RepositoryContainer(dataSourceContainer, retrofitContainer)
    }

    companion object DependencyContainer {
        lateinit var retrofitContainer: RetrofitContainer
        lateinit var serviceContainer: ServiceContainer
        lateinit var dataSourceContainer: DataSourceContainer
        lateinit var repositoryContainer: RepositoryContainer
    }
}
