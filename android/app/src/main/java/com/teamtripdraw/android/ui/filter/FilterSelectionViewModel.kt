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
import com.teamtripdraw.android.support.framework.presentation.event.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FilterSelectionViewModel @Inject constructor() : ViewModel() {

    private val _filterType = MutableLiveData<FilterType>()
    val filterType: LiveData<FilterType> = _filterType

    val years = OptionYear.values().toList()
    val months = OptionMonth.values().toList()
    val dayOfWeek = OptionDayOfWeek.values().toList()
    val hour = OptionHour.values().toList()
    val ageRange = OptionAgeRange.values().toList()
    val gender = OptionGender.values().toList()

    private val _address = MutableLiveData("")
    val address: LiveData<String> = _address

    private val _openAddressSelectionEvent = MutableLiveData<Boolean>()
    val openAddressSelectionEvent: LiveData<Boolean> = _openAddressSelectionEvent

    private val _openSearchResultEvent = MutableLiveData<Boolean>()
    val openSearchResultEvent: LiveData<Boolean> = _openSearchResultEvent

    private val _refreshEvent = MutableLiveData<Boolean>()
    val refreshEvent: LiveData<Boolean> = _refreshEvent

    private val _backPageEvent: MutableLiveData<Event<Boolean>> = MutableLiveData(Event(false))
    val backPageEvent: LiveData<Event<Boolean>> = _backPageEvent

    fun setupFilterType(type: FilterType) {
        _filterType.value = type
    }

    fun setAddress(address: String = "") {
        _address.value = address
    }

    fun openAddressSelection() {
        _openAddressSelectionEvent.value = true
    }

    fun openSearchResult() {
        _openSearchResultEvent.value = true
    }

    fun refresh() {
        _refreshEvent.value = true
    }

    fun backPage() {
        _backPageEvent.value = Event(true)
    }
}
