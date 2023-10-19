package com.teamtripdraw.android.domain.model.filterOption

import java.time.DayOfWeek

enum class OptionDayOfWeek(override val id: Int, override val value: DayOfWeek) : FilterOption<DayOfWeek> {
    SUNDAY(1, DayOfWeek.SUNDAY),
    MONDAY(2, DayOfWeek.MONDAY),
    TUESDAY(3, DayOfWeek.TUESDAY),
    WEDNESDAY(4, DayOfWeek.WEDNESDAY),
    THURSDAY(5, DayOfWeek.THURSDAY),
    FRIDAY(6, DayOfWeek.FRIDAY),
    SATURDAY(7, DayOfWeek.SATURDAY),
}
