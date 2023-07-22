package com.teamtripdraw.android.di

import com.teamtripdraw.android.data.repository.SetNickNameRepositoryImpl
import com.teamtripdraw.android.domain.repository.SetNickNameRepository

class RepositoryContainer(
    dataSourceContainer: DataSourceContainer,
    retrofitContainer: RetrofitContainer
) {
    val setNickNameRepository: SetNickNameRepository = SetNickNameRepositoryImpl(
        dataSourceContainer.remoteSetNickNameDataSource,
        retrofitContainer.tripDrawRetrofit
    )
}
