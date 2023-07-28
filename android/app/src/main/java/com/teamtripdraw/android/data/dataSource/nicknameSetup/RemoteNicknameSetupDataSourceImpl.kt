package com.teamtripdraw.android.data.dataSource.nicknameSetup

import com.teamtripdraw.android.data.dataSource.nicknameSetup.NicknameSetupDataSource
import com.teamtripdraw.android.data.httpClient.dto.request.NicknameSetUpRequest
import com.teamtripdraw.android.data.httpClient.dto.response.NicknameSetupResponse
import com.teamtripdraw.android.data.httpClient.retrofitAdapter.ResponseState
import com.teamtripdraw.android.data.httpClient.service.NicknameSetupService

class RemoteNicknameSetupDataSourceImpl(private val nicknameSetupService: NicknameSetupService) :
    NicknameSetupDataSource.Remote {
    override suspend fun setNickName(nickName: String): ResponseState<NicknameSetupResponse> =
        nicknameSetupService.setNickName(NicknameSetUpRequest(nickName))
}
