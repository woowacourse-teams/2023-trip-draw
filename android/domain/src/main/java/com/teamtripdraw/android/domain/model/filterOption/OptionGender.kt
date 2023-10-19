package com.teamtripdraw.android.domain.model.filterOption

enum class OptionGender(override val id: Int, override val value: String) : FilterOption<String> {
    FEMALE(1, "female"),
    MALE(2, "male"),
}
