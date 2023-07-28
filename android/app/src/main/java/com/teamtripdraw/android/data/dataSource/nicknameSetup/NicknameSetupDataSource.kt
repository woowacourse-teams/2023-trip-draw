package com.teamtripdraw.android.data.dataSource.nicknameSetup

import com.teamtripdraw.android.data.httpClient.dto.response.GetNicknameResponse
import com.teamtripdraw.android.data.httpClient.dto.response.NicknameSetupResponse
import com.teamtripdraw.android.data.httpClient.retrofitAdapter.ResponseState

interface NicknameSetupDataSource {
    interface Local
    interface Remote {
        suspend fun setNickname(nickname: String): ResponseState<NicknameSetupResponse>
        suspend fun getNickname(nicknameId: Long): ResponseState<GetNicknameResponse>
    }
}
