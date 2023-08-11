package com.teamtripdraw.android.data.dataSource.signUp

import com.teamtripdraw.android.data.model.DataUserInfo

interface SignUpDataSource {
    interface Local
    interface Remote {
        suspend fun setNickname(nickname: String): Result<Long>
        suspend fun getUserInfo(accessToken: String): Result<DataUserInfo>
    }
}
