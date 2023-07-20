package com.teamtripdraw.android.data.dataSource.remote

import com.teamtripdraw.android.data.httpClient.dto.response.SetNickNameResponse
import com.teamtripdraw.android.data.httpClient.retrofitAdapter.ResponseState

interface SetNickNameDataSource {
    suspend fun setNickName(nickName: String): ResponseState<SetNickNameResponse>
}
