package com.teamtripdraw.android.data.dataSource.setNickName.remote

import com.teamtripdraw.android.data.dataSource.setNickName.SetNickNameDataSource
import com.teamtripdraw.android.data.httpClient.dto.request.SetNickNameRequest
import com.teamtripdraw.android.data.httpClient.dto.response.SetNickNameResponse
import com.teamtripdraw.android.data.httpClient.retrofitAdapter.ResponseState
import com.teamtripdraw.android.data.httpClient.service.SetNickNameService

class RemoteSetNickNameDataSourceImpl(private val setNickNameService: SetNickNameService) :
    SetNickNameDataSource.Remote {
    override suspend fun setNickName(nickName: String): ResponseState<SetNickNameResponse> =
        setNickNameService.setNickName(SetNickNameRequest(nickName))
}
