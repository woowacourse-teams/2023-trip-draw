package com.teamtripdraw.android.data.httpClient.service

import com.teamtripdraw.android.data.dataSource.remote.SetNickNameDataSource
import com.teamtripdraw.android.data.httpClient.dto.request.SetNickNameRequest
import com.teamtripdraw.android.data.httpClient.dto.response.SetNickNameResponse
import com.teamtripdraw.android.data.httpClient.retrofitAdapter.ResponseState

class SetNickNameDataSourceImpl(private val setNickNameService: SetNickNameService) :
    SetNickNameDataSource {
    override suspend fun setNickName(nickName: String): ResponseState<SetNickNameResponse> =
        setNickNameService.setNickName(SetNickNameRequest(nickName))
}
