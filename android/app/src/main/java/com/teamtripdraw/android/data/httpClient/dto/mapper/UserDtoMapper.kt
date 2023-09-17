package com.teamtripdraw.android.data.httpClient.dto.mapper

import com.teamtripdraw.android.data.httpClient.dto.response.GetUserInfoResponse
import com.teamtripdraw.android.data.model.DataUserInfo

fun GetUserInfoResponse.toData(): DataUserInfo =
    DataUserInfo(
        memberId = memberId,
        nickname = nickname,
    )
