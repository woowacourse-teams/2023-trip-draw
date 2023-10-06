package com.teamtripdraw.android.ui.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.teamtripdraw.android.domain.model.filterOption.OptionAgeRange
import com.teamtripdraw.android.domain.model.filterOption.OptionDayOfWeek
import com.teamtripdraw.android.domain.model.filterOption.OptionGender
import com.teamtripdraw.android.domain.model.filterOption.OptionHour
import com.teamtripdraw.android.domain.model.filterOption.OptionMonth
import com.teamtripdraw.android.domain.model.filterOption.OptionYear

class FilterSelectionViewModel : ViewModel() {

    private val _filterType = MutableLiveData<FilterType>()
    val filterType: LiveData<FilterType> = _filterType

    val years = OptionYear.values().toList()
    val months = OptionMonth.values().toList()
    val dayOfWeek = OptionDayOfWeek.values().toList()
    val hour = OptionHour.values().toList()
    val ageRange = OptionAgeRange.values().toList()
    val gender = OptionGender.values().toList()

    private val _openSearchResultEvent = MutableLiveData<Boolean>()
    val openSearchResultEvent: LiveData<Boolean> = _openSearchResultEvent

    private val _refreshEvent = MutableLiveData<Boolean>()
    val refreshEvent: LiveData<Boolean> = _refreshEvent

    fun setupFilterType(type: FilterType) {
        _filterType.value = type
    }

    fun openSearchResult() {
        _openSearchResultEvent.value = true
    }

    fun refresh() {
        _refreshEvent.value = true
    }
}
