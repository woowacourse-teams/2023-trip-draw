package com.teamtripdraw.android.data.model.mapper

import com.teamtripdraw.android.data.model.DataLoginInfo
import com.teamtripdraw.android.domain.model.auth.LoginInfo

fun LoginInfo.toData(): DataLoginInfo =
    DataLoginInfo(
        platform = platform.name,
        socialToken = socialToken,
    )
