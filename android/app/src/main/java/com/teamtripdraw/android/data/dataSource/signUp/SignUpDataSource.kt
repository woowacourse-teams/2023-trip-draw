package com.teamtripdraw.android.data.dataSource.signUp

interface SignUpDataSource {
    interface Local
    interface Remote {
        suspend fun setNickname(nickname: String): Result<Long>
        suspend fun getUserInfo(accessToken: String): Result<String>
    }
}
