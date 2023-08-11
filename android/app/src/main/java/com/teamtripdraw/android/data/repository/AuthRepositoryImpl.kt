package com.teamtripdraw.android.data.repository

import com.teamtripdraw.android.data.dataSource.signUp.SignUpDataSource
import com.teamtripdraw.android.data.dataSource.userIdentifyInfo.UserIdentifyInfoDataSource
import com.teamtripdraw.android.data.model.mapper.toData
import com.teamtripdraw.android.domain.model.auth.LoginInfo
import com.teamtripdraw.android.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val localUserIdentifyInfoDataSource: UserIdentifyInfoDataSource.Local,
    private val remoteSignUpDataSource: SignUpDataSource.Remote,
    private val remoteUserIdentifyInfoDataSourceImpl: UserIdentifyInfoDataSource.Remote,
) :
    AuthRepository {
    override suspend fun setNickname(nickname: String): Result<Long> =
        remoteSignUpDataSource.setNickname(nickname)

    override suspend fun getNickname(nicknameId: Long): Result<String> =
        // todo #107 기준 로그인이 구현 안 되어 있는 문제로 임시로 닉네임을 통한 인증 상태 추후 로직 변경 필수(이슈 참고)
        remoteSignUpDataSource.getNickname(nicknameId).onSuccess { nickname ->
            localUserIdentifyInfoDataSource.setIdentifyInfo(nickname)
        }

    override suspend fun login(loginInfo: LoginInfo): Result<Boolean> =
        remoteUserIdentifyInfoDataSourceImpl.issueUserIdentifyInfo(loginInfo.toData())
            .onSuccess {
                if (it.isNotBlank()) localUserIdentifyInfoDataSource.setIdentifyInfo(it)
            }.map { it.isNotBlank() }
}
