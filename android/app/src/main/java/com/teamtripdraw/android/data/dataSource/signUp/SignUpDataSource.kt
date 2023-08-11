package com.teamtripdraw.android.data.dataSource.signUp

import com.teamtripdraw.android.data.model.DataLoginInfo
import com.teamtripdraw.android.data.model.DataUserInfo

interface SignUpDataSource {
    interface Local
    interface Remote {
        suspend fun setNickname(nickname: String, dataLoginInfo: DataLoginInfo): Result<String>
        suspend fun getUserInfo(accessToken: String): Result<DataUserInfo>
    }
}
