package com.teamtripdraw.android.domain.repository

interface AuthRepository {
    suspend fun setNickname(nickname: String): Result<Long>
    suspend fun getNickname(nicknameId: Long): Result<String>
}
