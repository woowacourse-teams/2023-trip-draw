package com.teamtripdraw.android.data.model.mapper

import com.teamtripdraw.android.data.model.DataUserInfo
import com.teamtripdraw.android.domain.model.user.UserInfo

fun DataUserInfo.toDomain(): UserInfo =
    UserInfo(
        memberId = memberId,
        nickname = nickname,
    )
