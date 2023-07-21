package com.teamtripdraw.android.domain.repository

interface SetNickNameRepository {
    suspend fun setNickName(nickName: String): Result<Unit>
}
