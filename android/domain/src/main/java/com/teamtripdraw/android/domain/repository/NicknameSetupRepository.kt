package com.teamtripdraw.android.domain.repository

interface NicknameSetupRepository {
    suspend fun setNickName(nickName: String): Result<Unit>
}
