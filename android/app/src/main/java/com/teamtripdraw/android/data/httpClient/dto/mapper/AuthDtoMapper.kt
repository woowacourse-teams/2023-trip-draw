package com.teamtripdraw.android.data.httpClient.dto.mapper

import com.teamtripdraw.android.data.httpClient.dto.request.LoginRequest
import com.teamtripdraw.android.data.httpClient.dto.response.LoginResponse
import com.teamtripdraw.android.data.httpClient.dto.response.NicknameSetupResponse
import com.teamtripdraw.android.data.httpClient.dto.response.TokenRefreshResponse
import com.teamtripdraw.android.data.model.DataLoginInfo
import com.teamtripdraw.android.data.model.DataUserIdentifyInfo

fun DataLoginInfo.toHttpRequest(): LoginRequest =
    LoginRequest(
        oauthType = platform,
        oauthToken = socialToken,
    )

fun LoginResponse.toData(): DataUserIdentifyInfo =
    DataUserIdentifyInfo(
        accessToken = accessToken,
        refreshToken = refreshToken,
    )

fun NicknameSetupResponse.toData(): DataUserIdentifyInfo =
    DataUserIdentifyInfo(
        accessToken = accessToken,
        refreshToken = refreshToken,
    )

fun TokenRefreshResponse.toData(): DataUserIdentifyInfo =
    DataUserIdentifyInfo(
        accessToken = accessToken,
        refreshToken = refreshToken,
    )
