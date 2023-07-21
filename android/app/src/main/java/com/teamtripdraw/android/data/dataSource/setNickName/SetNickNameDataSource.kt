package com.teamtripdraw.android.data.dataSource.setNickName

import com.teamtripdraw.android.data.httpClient.dto.response.SetNickNameResponse
import com.teamtripdraw.android.data.httpClient.retrofitAdapter.ResponseState

interface SetNickNameDataSource {
    interface Local
    interface Remote {
        suspend fun setNickName(nickName: String): ResponseState<SetNickNameResponse>
    }
}
