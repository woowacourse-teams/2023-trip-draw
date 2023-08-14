package com.teamtripdraw.android.domain.model.auth

data class LoginInfo(
    val platform: LoginPlatform,
    val socialToken: String,
)
