package com.teamtripdraw.android.data.httpClient.dto.mapper

import com.teamtripdraw.android.data.httpClient.dto.request.LoginRequest
import com.teamtripdraw.android.data.model.DataLoginInfo

fun DataLoginInfo.toHttpRequest(): LoginRequest =
    LoginRequest(
        oauthType = platform,
        oauthToken = socialToken,
    )
