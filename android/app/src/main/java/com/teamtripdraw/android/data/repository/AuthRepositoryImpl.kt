package com.teamtripdraw.android.data.repository

import com.teamtripdraw.android.data.dataSource.signUp.SignUpDataSource
import com.teamtripdraw.android.data.dataSource.userIdentifyInfo.UserIdentifyInfoDataSource
import com.teamtripdraw.android.data.model.mapper.toData
import com.teamtripdraw.android.data.model.mapper.toDomain
import com.teamtripdraw.android.domain.model.auth.LoginInfo
import com.teamtripdraw.android.domain.model.user.UserInfo
import com.teamtripdraw.android.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val localUserIdentifyInfoDataSource: UserIdentifyInfoDataSource.Local,
    private val remoteSignUpDataSource: SignUpDataSource.Remote,
    private val remoteUserIdentifyInfoDataSourceImpl: UserIdentifyInfoDataSource.Remote,
) :
    AuthRepository {
    override suspend fun setNickname(nickname: String): Result<Long> =
        remoteSignUpDataSource.setNickname(nickname)

    override suspend fun getUserInfo(): Result<UserInfo> =
        remoteSignUpDataSource.getUserInfo(localUserIdentifyInfoDataSource.getIdentifyInfo()).map {
            it.toDomain()
        }

    override suspend fun login(loginInfo: LoginInfo): Result<Boolean> =
        remoteUserIdentifyInfoDataSourceImpl.issueUserIdentifyInfo(loginInfo.toData())
            .onSuccess {
                if (it.isNotBlank()) localUserIdentifyInfoDataSource.setIdentifyInfo(it)
            }.map { it.isNotBlank() }
}
