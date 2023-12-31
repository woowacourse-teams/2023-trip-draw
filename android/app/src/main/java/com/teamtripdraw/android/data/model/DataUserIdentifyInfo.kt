package com.teamtripdraw.android.data.model

data class DataUserIdentifyInfo(
    val accessToken: String,
    val refreshToken: String,
) {
    fun isNotBlank(): Boolean =
        accessToken.isNotBlank() && refreshToken.isNotBlank()
}
