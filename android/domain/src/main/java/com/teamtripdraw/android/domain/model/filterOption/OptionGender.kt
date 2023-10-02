package com.teamtripdraw.android.domain.model.filterOption

enum class OptionGender(override val id: Int, override val value: Any) : FilterOption {
    FEMALE(1, "female"),
    MALE(2, "male"),
}
