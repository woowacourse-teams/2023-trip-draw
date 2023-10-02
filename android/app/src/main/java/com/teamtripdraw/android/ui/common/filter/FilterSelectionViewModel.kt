package com.teamtripdraw.android.ui.common.filter

import androidx.lifecycle.ViewModel
import com.teamtripdraw.android.domain.model.filterOption.OptionAgeRange
import com.teamtripdraw.android.domain.model.filterOption.OptionDayOfWeek
import com.teamtripdraw.android.domain.model.filterOption.OptionGender
import com.teamtripdraw.android.domain.model.filterOption.OptionHour
import com.teamtripdraw.android.domain.model.filterOption.OptionMonth
import com.teamtripdraw.android.domain.model.filterOption.OptionYear

class FilterSelectionViewModel : ViewModel() {

    val years = OptionYear.values().toList()
    val months = OptionMonth.values().toList()
    val dayOfWeek = OptionDayOfWeek.values().toList()
    val hour = OptionHour.values().toList()
    val ageRange = OptionAgeRange.values().toList()
    val gender = OptionGender.values().toList()
}
