package com.teamtripdraw.android.data.dataSource.auth.login

import com.teamtripdraw.android.data.httpClient.dto.mapper.toData
import com.teamtripdraw.android.data.httpClient.dto.mapper.toHttpRequest
import com.teamtripdraw.android.data.httpClient.service.LoginService
import com.teamtripdraw.android.data.model.DataLoginInfo
import com.teamtripdraw.android.data.model.DataUserIdentifyInfo
import javax.inject.Inject

class RemoteLoginDataSourceImpl @Inject constructor(
    private val loginService: LoginService,
) : LoginDataSource.Remote {
    override suspend fun login(dataLoginInfo: DataLoginInfo): Result<DataUserIdentifyInfo> =
        loginService.login(dataLoginInfo.toHttpRequest()).process { body, headers ->
            Result.success(body.toData())
        }
}
