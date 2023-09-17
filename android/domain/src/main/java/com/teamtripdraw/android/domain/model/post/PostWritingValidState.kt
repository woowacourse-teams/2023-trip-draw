package com.teamtripdraw.android.domain.model.post

enum class PostWritingValidState {
    EMPTY_TITLE, EMPTY_WRITING, AVAILABLE;

    companion object {
        const val MAX_TITLE_LENGTH = 100
        const val MAX_WRITING_LENGTH = 10000

        fun getValidState(title: String, writing: String): PostWritingValidState {
            return when {
                title.isEmpty() || title.replace(" ", "").isEmpty() -> EMPTY_TITLE
                writing.isEmpty() || writing.replace(" ", "").isEmpty() -> EMPTY_WRITING
                else -> AVAILABLE
            }
        }
    }
}
