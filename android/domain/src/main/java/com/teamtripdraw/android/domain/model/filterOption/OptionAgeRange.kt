package com.teamtripdraw.android.domain.model.filterOption

enum class OptionAgeRange(override val id: Int, override val value: Any) : FilterOption {
    AGE_1_TO_9(1, IntRange(1, 9)),
    AGE_10_TO_14(2, IntRange(10, 14)),
    AGE_15_TO_19(3, IntRange(15, 19)),
    AGE_20_TO_29(4, IntRange(20, 29)),
    AGE_30_TO_39(5, IntRange(30, 39)),
    AGE_40_TO_49(6, IntRange(40, 49)),
    AGE_50_TO_59(7, IntRange(50, 59)),
    AGE_60_TO_69(8, IntRange(60, 69)),
    AGE_70_TO_79(9, IntRange(70, 79)),
    AGE_80_TO_89(10, IntRange(80, 89)),
    AGE_OVER_90(11, IntRange(90, 0)),
}
