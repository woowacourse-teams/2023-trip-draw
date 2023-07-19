package com.teamtripdraw.android.ui.signUp

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.teamtripdraw.android.R

enum class NicknameState(
    @StringRes val warningMessage: Int?,
    val warningState: Boolean,
    val completeBtnEnabled: Boolean,
    @DrawableRes val completeBtnDrawable: Int
) {
    INITIAL(null, false, false, R.drawable.shape_td_gray_fill_12_rect),
    AVAILABLE(null, false, true, R.drawable.shape_td_sub_blue_fill_12_rect),
    CONTAIN_BLANK(R.string.warning_contain_blank, true, false, R.drawable.shape_td_gray_fill_12_rect),
    OVER_LENGTH(R.string.warning_over_length, true, false, R.drawable.shape_td_gray_fill_12_rect);

    companion object {
        private const val MAX_NAME_LENGTH = 10
        private const val MIN_NAME_LENGTH = 1
        private const val WRONG_NICKNAME_STATE = "닉네임의 상태가 올바르지 않습니다."

        fun getState(nickname: String): NicknameState {
            return when {
                nickname.isEmpty() -> INITIAL
                MAX_NAME_LENGTH < nickname.length -> OVER_LENGTH
                nickname.contains(" ") -> CONTAIN_BLANK
                nickname.length in MIN_NAME_LENGTH..MAX_NAME_LENGTH -> AVAILABLE
                else -> throw IllegalArgumentException(WRONG_NICKNAME_STATE)
            }
        }
    }
}