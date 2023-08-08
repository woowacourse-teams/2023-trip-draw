package com.teamtripdraw.android.ui.common.dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamtripdraw.android.domain.model.trip.TripStatus
import com.teamtripdraw.android.domain.model.trip.TripTitleValidState
import com.teamtripdraw.android.domain.repository.TripRepository
import com.teamtripdraw.android.support.framework.presentation.event.Event
import com.teamtripdraw.android.ui.model.UiHistoryItem
import kotlinx.coroutines.launch

class SetTripTitleDialogViewModel(
    private val repository: TripRepository
) : ViewModel() {

    val MAX_INPUT_TITLE_LENGTH = TripTitleValidState.MAX_TITLE_LENGTH + 1

    private val _tripId: MutableLiveData<Long> = MutableLiveData()
    val tripId: LiveData<Long> = _tripId

    val tripTitle: MutableLiveData<String> = MutableLiveData("")

    private val _titleState: MutableLiveData<TripTitleValidState> =
        MutableLiveData(TripTitleValidState.DEFAULT)
    val titleState: LiveData<TripTitleValidState> = _titleState

    private val _titleSetupCompletedEvent: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val titleSetupCompletedEvent: LiveData<Event<Boolean>> = _titleSetupCompletedEvent

    private val _tripItem: MutableLiveData<UiHistoryItem> = MutableLiveData()
    val tripItem: LiveData<UiHistoryItem> = _tripItem

    fun updateTripId(id: Long) {
        _tripId.value = id
    }

    fun titleChangedEvent() {
        _titleState.value = TripTitleValidState.getValidState(requireNotNull(tripTitle.value))
    }

    fun setTripTitle() {
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
