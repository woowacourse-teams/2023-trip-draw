package com.teamtripdraw.android.testDouble

import com.teamtripdraw.android.domain.model.auth.LoginInfo
import com.teamtripdraw.android.domain.model.auth.LoginPlatform
import com.teamtripdraw.android.ui.model.UiLoginInfo

internal fun DummyLoginInfo(
    platform: LoginPlatform = LoginPlatform.KAKAO,
    socialToken: String = "asdf",
): LoginInfo = LoginInfo(
    platform = platform,
    socialToken = socialToken,
)

internal fun DummyUiLoginInfo(
    platform: String = "KAKAO",
    socialToken: String = "asdf",
): UiLoginInfo = UiLoginInfo(
    platform = platform,
    socialToken = socialToken,
)
