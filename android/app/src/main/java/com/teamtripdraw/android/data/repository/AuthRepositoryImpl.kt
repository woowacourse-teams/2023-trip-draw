package com.teamtripdraw.android.data.repository

import com.teamtripdraw.android.data.dataSource.signUp.SignUpDataSource
import com.teamtripdraw.android.data.dataSource.unsubscribe.UnsubscribeDataSource
import com.teamtripdraw.android.data.dataSource.userIdentifyInfo.UserIdentifyInfoDataSource
import com.teamtripdraw.android.data.model.mapper.toData
import com.teamtripdraw.android.data.model.mapper.toDomain
import com.teamtripdraw.android.domain.model.auth.LoginInfo
import com.teamtripdraw.android.domain.model.user.UserInfo
import com.teamtripdraw.android.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val localUserIdentifyInfoDataSource: UserIdentifyInfoDataSource.Local,
    private val remoteSignUpDataSource: SignUpDataSource.Remote,
    private val remoteUserIdentifyInfoDataSource: UserIdentifyInfoDataSource.Remote,
    private val remoteUnsubscribeDataSource: UnsubscribeDataSource.Remote,
) :
    AuthRepository {
    override suspend fun setNickname(nickname: String, loginInfo: LoginInfo): Result<Unit> =
        remoteSignUpDataSource.setNickname(nickname, loginInfo.toData())
            .onSuccess { localUserIdentifyInfoDataSource.setIdentifyInfo(it) }
            .map {}

    override suspend fun getUserInfo(): Result<UserInfo> =
        remoteSignUpDataSource.getUserInfo(localUserIdentifyInfoDataSource.getIdentifyInfo()).map {
            it.toDomain()
        }

    override suspend fun login(loginInfo: LoginInfo): Result<Boolean> =
        remoteUserIdentifyInfoDataSource.issueUserIdentifyInfo(loginInfo.toData())
            .onSuccess {
                if (it.isNotBlank()) localUserIdentifyInfoDataSource.setIdentifyInfo(it)
            }.map { it.isNotBlank() }

    override fun getAutoLoginState(): Boolean =
        localUserIdentifyInfoDataSource.getIdentifyInfo().isNotBlank()

    override fun logout() {
        localUserIdentifyInfoDataSource.deleteIdentifyInfo()
    }

    override suspend fun unsubscribe(): Result<Unit> =
        remoteUnsubscribeDataSource.unsubscribe(localUserIdentifyInfoDataSource.getIdentifyInfo())
            .onSuccess { localUserIdentifyInfoDataSource.deleteIdentifyInfo() }
}
