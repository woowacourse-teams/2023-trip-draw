package com.teamtripdraw.android.domain.model.filterOption

import java.time.Month

enum class OptionMonth(override val id: Int, override val value: Any) : FilterOption {
    JANUARY(1, Month.JANUARY),
    FEBRUARY(2, Month.FEBRUARY),
    MARCH(3, Month.MARCH),
    APRIL(4, Month.APRIL),
    MAY(5, Month.MAY),
    JUNE(6, Month.JUNE),
    JULY(7, Month.JULY),
    AUGUST(8, Month.AUGUST),
    SEPTEMBER(9, Month.SEPTEMBER),
    OCTOBER(10, Month.OCTOBER),
    NOVEMBER(11, Month.NOVEMBER),
    DECEMBER(12, Month.DECEMBER),
}
