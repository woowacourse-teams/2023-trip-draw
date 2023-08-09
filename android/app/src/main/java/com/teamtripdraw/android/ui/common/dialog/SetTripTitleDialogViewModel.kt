package com.teamtripdraw.android.ui.common.dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamtripdraw.android.domain.constants.NULL_SUBSTITUTE_TRIP_ID
import com.teamtripdraw.android.domain.model.trip.TripStatus
import com.teamtripdraw.android.domain.model.trip.TripTitleValidState
import com.teamtripdraw.android.domain.repository.TripRepository
import com.teamtripdraw.android.support.framework.presentation.event.Event
import com.teamtripdraw.android.ui.model.UiTripItem
import kotlinx.coroutines.launch

class SetTripTitleDialogViewModel(
    private val repository: TripRepository
) : ViewModel() {

    val MAX_INPUT_TITLE_LENGTH = TripTitleValidState.MAX_TITLE_LENGTH + 1

    var tripId: Long = NULL_SUBSTITUTE_TRIP_ID
        private set

    val tripTitle: MutableLiveData<String> = MutableLiveData("")

    private val _titleState: MutableLiveData<TripTitleValidState> =
        MutableLiveData(TripTitleValidState.AVAILABLE)
    val titleState: LiveData<TripTitleValidState> = _titleState

    private val _titleSetupCompletedEvent: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val titleSetupCompletedEvent: LiveData<Event<Boolean>> = _titleSetupCompletedEvent

    private val _tripItem: MutableLiveData<UiTripItem> = MutableLiveData()
    val tripItem: LiveData<UiTripItem> = _tripItem

    fun updateTripId(id: Long) {
        tripId = id
    }

    fun titleChangedEvent() {
        _titleState.value = TripTitleValidState.getValidState(requireNotNull(tripTitle.value))
    }

    fun setTripTitle() {
        viewModelScope.launch {
            repository.setTripTitle(
                tripId = tripId,
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
