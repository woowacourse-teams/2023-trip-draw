package com.teamtripdraw.android.ui.filter

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SelectedOptions(
    val years: List<Int>,
    val months: List<Int>,
    val daysOfWeek: List<Int>,
    val ageRanges: List<Int>,
    val genders: List<Int>,
    val address: String,
    val hours: List<Int>?,
) : Parcelable

class SelectedOptionsBuilder {
    private val years = mutableListOf<Int>()
    private val months = mutableListOf<Int>()
    private val daysOfWeek = mutableListOf<Int>()
    private val ageRanges = mutableListOf<Int>()
    private val genders = mutableListOf<Int>()
    private var address = ""
    private var hours: List<Int>? = null

    fun build(): SelectedOptions =
        SelectedOptions(years, months, daysOfWeek, ageRanges, genders, address, hours)

    fun setYears(year: List<Int>): SelectedOptionsBuilder {
        years.addAll(year)
        return this
    }

    fun setMonths(month: List<Int>): SelectedOptionsBuilder {
        months.addAll(month)
        return this
    }

    fun setDaysOfWeek(dayOfWeek: List<Int>): SelectedOptionsBuilder {
        daysOfWeek.addAll(dayOfWeek)
        return this
    }

    fun setAgeRanges(ageRange: List<Int>): SelectedOptionsBuilder {
        ageRanges.addAll(ageRange)
        return this
    }

    fun setGenders(gender: List<Int>): SelectedOptionsBuilder {
        genders.addAll(gender)
        return this
    }

    fun setAddress(address: String): SelectedOptionsBuilder {
        this.address = address
        return this
    }

    fun setHours(hour: List<Int>?): SelectedOptionsBuilder {
        hours = hour
        return this
    }
}
