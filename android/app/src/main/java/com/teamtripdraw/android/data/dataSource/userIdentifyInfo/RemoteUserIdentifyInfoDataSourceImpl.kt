package com.teamtripdraw.android.data.dataSource.userIdentifyInfo

import com.teamtripdraw.android.data.httpClient.dto.mapper.toHttpRequest
import com.teamtripdraw.android.data.httpClient.service.LoginService
import com.teamtripdraw.android.data.model.DataLoginInfo

class RemoteUserIdentifyInfoDataSourceImpl(
    private val loginService: LoginService,
) : UserIdentifyInfoDataSource.Remote {
    override suspend fun issueUserIdentifyInfo(dataLoginInfo: DataLoginInfo) {
        loginService.login(dataLoginInfo.toHttpRequest())
    }
}
