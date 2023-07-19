package com.teamtripdraw.android.domain.user

enum class NicknameValidState {
    EMPTY, AVAILABLE, EXCEED_LIMIT, CONTAIN_BLANK;

    companion object {
        const val MAX_NAME_LENGTH = 10
        const val MIN_NAME_LENGTH = 1
        private const val WRONG_NICKNAME_STATE = "닉네임의 상태가 올바르지 않습니다."

        fun getValidState(nickname: String): NicknameValidState {
            return when {
                nickname.isEmpty() -> EMPTY
                MAX_NAME_LENGTH < nickname.length -> EXCEED_LIMIT
                nickname.contains(" ") -> CONTAIN_BLANK
                nickname.length in MIN_NAME_LENGTH..MAX_NAME_LENGTH -> AVAILABLE
                else -> throw IllegalArgumentException(WRONG_NICKNAME_STATE)
            }
        }
    }
}