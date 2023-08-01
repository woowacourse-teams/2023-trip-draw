package com.teamtripdraw.android.data.repository

import com.teamtripdraw.android.data.dataSource.nicknameSetup.NicknameSetupDataSource
import com.teamtripdraw.android.data.dataSource.userIdentifyInfo.UserIdentifyInfoDataSource
import com.teamtripdraw.android.domain.repository.NicknameSetupRepository

class NicknameSetupRepositoryImpl(
    private val localUserIdentifyInfoDataSource: UserIdentifyInfoDataSource.Local,
    private val remoteNicknameSetupDataSource: NicknameSetupDataSource.Remote
) :
    NicknameSetupRepository {
    override suspend fun setNickname(nickname: String): Result<Long> =
        remoteNicknameSetupDataSource.setNickname(nickname)

    override suspend fun getNickname(nicknameId: Long): Result<String> =
        // todo #107 기준 로그인이 구현 안 되어 있는 문제로 임시로 닉네임을 통한 인증 상태 추후 로직 변경 필수(이슈 참고)
        remoteNicknameSetupDataSource.getNickname(nicknameId).onSuccess { nickname ->
            localUserIdentifyInfoDataSource.setIdentifyInfo(nickname)
        }
}
