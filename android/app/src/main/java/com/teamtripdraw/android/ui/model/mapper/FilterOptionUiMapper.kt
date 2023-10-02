package com.teamtripdraw.android.ui.model.mapper

import com.teamtripdraw.android.domain.model.filterOption.FilterOption
import com.teamtripdraw.android.domain.model.filterOption.OptionAgeRange
import com.teamtripdraw.android.domain.model.filterOption.OptionDayOfWeek
import com.teamtripdraw.android.domain.model.filterOption.OptionGender
import com.teamtripdraw.android.domain.model.filterOption.OptionHour
import com.teamtripdraw.android.domain.model.filterOption.OptionMonth
import com.teamtripdraw.android.domain.model.filterOption.OptionYear

fun FilterOption.toText(): String {
    return when (this) {
        is OptionYear -> this.toText()
        is OptionMonth -> this.toText()
        is OptionDayOfWeek -> this.toText()
        is OptionHour -> this.value.toString()
        is OptionAgeRange -> this.toText()
        is OptionGender -> this.toText()
    }
}

private fun OptionYear.toText(): String = this.value.toString()

private fun OptionMonth.toText(): String =
    when (this) {
        OptionMonth.JANUARY -> "1월"
        OptionMonth.FEBRUARY -> "2월"
        OptionMonth.MARCH -> "3월"
        OptionMonth.APRIL -> "4월"
        OptionMonth.MAY -> "5월"
        OptionMonth.JUNE -> "6월"
        OptionMonth.JULY -> "7월"
        OptionMonth.AUGUST -> "8월"
        OptionMonth.SEPTEMBER -> "9월"
        OptionMonth.OCTOBER -> "10월"
        OptionMonth.NOVEMBER -> "11월"
        OptionMonth.DECEMBER -> "12월"
    }

private fun OptionDayOfWeek.toText(): String =
    when (this) {
        OptionDayOfWeek.SUNDAY -> "일"
        OptionDayOfWeek.MONDAY -> "월"
        OptionDayOfWeek.TUESDAY -> "화"
        OptionDayOfWeek.WEDNESDAY -> "수"
        OptionDayOfWeek.THURSDAY -> "목"
        OptionDayOfWeek.FRIDAY -> "금"
        OptionDayOfWeek.SATURDAY -> "토"
    }

private fun OptionAgeRange.toText(): String =
    when (this) {
        OptionAgeRange.AGE_1_TO_9 -> "1~9세"
        OptionAgeRange.AGE_10_TO_14 -> "10~14세"
        OptionAgeRange.AGE_15_TO_19 -> "15~19세"
        OptionAgeRange.AGE_20_TO_29 -> "20~29세"
        OptionAgeRange.AGE_30_TO_39 -> "30~39세"
        OptionAgeRange.AGE_40_TO_49 -> "40~49세"
        OptionAgeRange.AGE_50_TO_59 -> "50~59세"
        OptionAgeRange.AGE_60_TO_69 -> "60~69세"
        OptionAgeRange.AGE_70_TO_79 -> "70~79세"
        OptionAgeRange.AGE_80_TO_89 -> "80~89세"
        OptionAgeRange.AGE_OVER_90 -> "90세~"
    }

private fun OptionGender.toText(): String =
    when (this) {
        OptionGender.FEMALE -> "여성"
        OptionGender.MALE -> "남성"
    }
