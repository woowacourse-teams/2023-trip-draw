package com.teamtripdraw.android.domain.repository

interface NicknameSetupRepository {
    suspend fun setNickName(nickname: String): Result<Long>
    suspend fun getNickName(nicknameId: Long): Result<Unit>
}
