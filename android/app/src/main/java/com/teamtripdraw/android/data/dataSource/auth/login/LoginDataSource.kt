package com.teamtripdraw.android.data.dataSource.auth.login

import com.teamtripdraw.android.data.model.DataLoginInfo
import com.teamtripdraw.android.data.model.DataUserIdentifyInfo

interface LoginDataSource {
    interface Local
    interface Remote {
        suspend fun login(dataLoginInfo: DataLoginInfo): Result<DataUserIdentifyInfo>
    }
}
