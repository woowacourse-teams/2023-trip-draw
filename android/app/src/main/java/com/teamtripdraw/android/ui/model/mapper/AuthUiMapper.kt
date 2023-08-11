package com.teamtripdraw.android.ui.model.mapper

import com.teamtripdraw.android.domain.model.auth.LoginInfo
import com.teamtripdraw.android.domain.model.auth.LoginPlatform
import com.teamtripdraw.android.ui.model.UiLoginInfo

fun LoginInfo.toPresentation() = UiLoginInfo(
    platform = platform.name,
    socialToken = socialToken,
)

fun UiLoginInfo.toDomain() = LoginInfo(
    platform = LoginPlatform.getLoginPlatform(platform),
    socialToken = socialToken,
)
