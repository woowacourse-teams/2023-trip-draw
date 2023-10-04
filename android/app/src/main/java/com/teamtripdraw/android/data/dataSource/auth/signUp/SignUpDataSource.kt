package com.teamtripdraw.android.data.dataSource.auth.signUp

import com.teamtripdraw.android.data.model.DataLoginInfo
import com.teamtripdraw.android.data.model.DataUserIdentifyInfo
import com.teamtripdraw.android.data.model.DataUserInfo

interface SignUpDataSource {
    interface Local
    interface Remote {
        suspend fun setNickname(
            nickname: String,
            dataLoginInfo: DataLoginInfo,
        ): Result<DataUserIdentifyInfo>

        suspend fun getUserInfo(): Result<DataUserInfo>
    }
}
