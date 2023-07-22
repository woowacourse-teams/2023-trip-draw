package com.teamtripdraw.android.di

import com.teamtripdraw.android.data.repository.NicknameSetupRepositoryImpl
import com.teamtripdraw.android.domain.repository.NicknameSetupRepository

class RepositoryContainer(
    dataSourceContainer: DataSourceContainer,
    retrofitContainer: RetrofitContainer
) {
    val nicknameSetupRepository: NicknameSetupRepository = NicknameSetupRepositoryImpl(
        dataSourceContainer.remoteNicknameSetupDataSource,
        retrofitContainer.tripDrawRetrofit
    )
}
