package com.teamtripdraw.android.domain.repository

import com.teamtripdraw.android.domain.model.auth.LoginInfo

interface AuthRepository {
    suspend fun setNickname(nickname: String): Result<Long>
    suspend fun getNickname(nicknameId: Long): Result<String>
    suspend fun login(loginInfo: LoginInfo): Result<Boolean>
}
