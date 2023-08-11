package com.teamtripdraw.android.domain.repository

import com.teamtripdraw.android.domain.model.auth.LoginInfo
import com.teamtripdraw.android.domain.model.user.UserInfo

interface AuthRepository {
    suspend fun setNickname(nickname: String): Result<Long>
    suspend fun getUserInfo(): Result<UserInfo>
    suspend fun login(loginInfo: LoginInfo): Result<Boolean>
}
