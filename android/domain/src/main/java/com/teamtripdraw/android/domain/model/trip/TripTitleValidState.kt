package com.teamtripdraw.android.domain.model.trip

enum class TripTitleValidState {
    DEFAULT, EXCEED_LIMIT, CONTAIN_BLANK;

    companion object {
        const val MAX_TITLE_LENGTH = 100
        const val MIN_TITLE_LENGTH = 1
        private const val WRONG_NICKNAME_STATE = "여행 제목이 올바르지 않습니다."

        fun getValidState(title: String): TripTitleValidState {
            return when {
                title.isBlank() -> CONTAIN_BLANK
                MAX_TITLE_LENGTH < title.length -> EXCEED_LIMIT
                title.length in MIN_TITLE_LENGTH..MAX_TITLE_LENGTH -> DEFAULT
                else -> throw IllegalArgumentException(WRONG_NICKNAME_STATE)
            }
        }
    }
}
