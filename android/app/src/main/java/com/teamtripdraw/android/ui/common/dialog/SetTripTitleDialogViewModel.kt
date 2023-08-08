package com.teamtripdraw.android.ui.common.dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamtripdraw.android.domain.model.trip.TripStatus
import com.teamtripdraw.android.domain.repository.TripRepository
import com.teamtripdraw.android.support.framework.presentation.event.Event
import kotlinx.coroutines.launch

class SetTripTitleDialogViewModel(
    private val repository: TripRepository
) : ViewModel() {

    val MAX_INPUT_TITLE_LENGTH = 100

    private val _tripId: MutableLiveData<Long> = MutableLiveData()
    val tripId: LiveData<Long> = _tripId

    val tripTitle: MutableLiveData<String> = MutableLiveData("")

    private val _isBlankEvent: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val isBlankEvent: LiveData<Event<Boolean>> = _isBlankEvent

    private val _titleSetupCompletedEvent: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val titleSetupCompletedEvent: LiveData<Event<Boolean>> = _titleSetupCompletedEvent

    fun updateTripId(id: Long) {
        _tripId.value = id
    }

    fun setTripTitle() {
        if (tripTitle.value!!.isBlank()) {
            _isBlankEvent.value = Event(true)
        } else {
            viewModelScope.launch {
                repository.setTripTitle(
                    tripId = requireNotNull(tripId.value),
                    name = requireNotNull(tripTitle.value),
                    status = TripStatus.FINISHED
                ).onSuccess {
                    _titleSetupCompletedEvent.value = Event(true)
                }.onFailure {
                    // todo 오류 처리
                }
            }
        }
    }
}
