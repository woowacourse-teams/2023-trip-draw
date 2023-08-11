package com.teamtripdraw.android.domain.model.auth

enum class LoginPlatform {
    KAKAO,
    ;

    companion object {
        private const val INVALID_PLATFORM_NAME_ERROR = "LoginPlatform에 존재하지 않는 플랫폼 명이 입력 되었습니다."

        fun getLoginPlatform(platformName: String): LoginPlatform =
            values().find { platformName == it.name } ?: throw IllegalStateException(
                INVALID_PLATFORM_NAME_ERROR,
            )
    }
}
