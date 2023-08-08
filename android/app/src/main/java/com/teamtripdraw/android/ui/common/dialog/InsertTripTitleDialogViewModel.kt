package com.teamtripdraw.android.ui.common.dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.teamtripdraw.android.domain.repository.TripRepository

class InsertTripTitleDialogViewModel(
    private val repository: TripRepository
) : ViewModel() {

    val MAX_INPUT_TITLE_LENGTH = 100

    private val _tripId: MutableLiveData<Long> = MutableLiveData()
    val tripId: LiveData<Long> = _tripId

    val tripTitle: MutableLiveData<String> = MutableLiveData("")


    fun updateTripId(id: Long) {
        _tripId.value = id
    }

    fun setTripTitle() {

    }
}
