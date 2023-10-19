package com.teamtripdraw.android.ui.filter

import android.os.Parcelable
import com.teamtripdraw.android.domain.model.filterOption.OptionAgeRange
import com.teamtripdraw.android.domain.model.filterOption.OptionDayOfWeek
import com.teamtripdraw.android.domain.model.filterOption.OptionGender
import com.teamtripdraw.android.domain.model.filterOption.OptionMonth
import com.teamtripdraw.android.domain.model.filterOption.OptionYear
import kotlinx.parcelize.Parcelize
import kotlin.reflect.typeOf

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

    fun setSelectedOptions(
        type: FilterView,
        options: List<Int>,
    ) {
        when (type.type) {
            typeOf<OptionYear>() -> years.addAll(options)
            typeOf<OptionMonth>() -> months.addAll(options)
            typeOf<OptionDayOfWeek>() -> daysOfWeek.addAll(options)
            typeOf<OptionAgeRange>() -> ageRanges.addAll(options)
            typeOf<OptionGender>() -> genders.addAll(options)
        }
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
