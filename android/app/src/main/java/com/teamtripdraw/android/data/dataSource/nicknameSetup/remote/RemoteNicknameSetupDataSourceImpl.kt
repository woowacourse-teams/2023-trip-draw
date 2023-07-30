package com.teamtripdraw.android.data.dataSource.nicknameSetup.remote

import com.teamtripdraw.android.data.dataSource.nicknameSetup.NicknameSetupDataSource
import com.teamtripdraw.android.data.httpClient.dto.request.NicknameSetUpRequest
import com.teamtripdraw.android.data.httpClient.dto.response.GetNicknameResponse
import com.teamtripdraw.android.data.httpClient.dto.response.NicknameSetupResponse
import com.teamtripdraw.android.data.httpClient.retrofitAdapter.ResponseState
import com.teamtripdraw.android.data.httpClient.service.GetNicknameService
import com.teamtripdraw.android.data.httpClient.service.NicknameSetupService

class RemoteNicknameSetupDataSourceImpl(
    private val nicknameSetupService: NicknameSetupService,
    private val getNicknameService: GetNicknameService
) :
    NicknameSetupDataSource.Remote {
    override suspend fun setNickname(nickname: String): ResponseState<NicknameSetupResponse> =
        nicknameSetupService.setNickname(NicknameSetUpRequest(nickname))

    override suspend fun getNickname(nicknameId: Long): ResponseState<GetNicknameResponse> =
        getNicknameService.getNickname(nicknameId)
}
