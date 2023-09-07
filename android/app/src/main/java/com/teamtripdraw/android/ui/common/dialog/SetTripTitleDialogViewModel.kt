package com.teamtripdraw.android.ui.common.dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamtripdraw.android.TripDrawApplication
import com.teamtripdraw.android.domain.constants.NULL_SUBSTITUTE_TRIP_ID
import com.teamtripdraw.android.domain.model.trip.PreSetTripTitle
import com.teamtripdraw.android.domain.model.trip.PreviewTrip
import com.teamtripdraw.android.domain.model.trip.TripStatus
import com.teamtripdraw.android.domain.model.trip.TripTitleValidState
import com.teamtripdraw.android.domain.repository.TripRepository
import com.teamtripdraw.android.support.framework.presentation.event.Event
import com.teamtripdraw.android.ui.model.UiPreviewTrip
import com.teamtripdraw.android.ui.model.mapper.toDomain
import com.teamtripdraw.android.ui.model.mapper.toPresentation
import com.teamtripdraw.android.ui.model.mapper.toPreviewPresentation
import kotlinx.coroutines.launch

class SetTripTitleDialogViewModel(
    private val repository: TripRepository,
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

    private val _previewTrip: MutableLiveData<PreviewTrip> = MutableLiveData()
    val previewTrip: LiveData<UiPreviewTrip> =
        Transformations.map(_previewTrip) { trip -> trip.toPresentation() }

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
                preSetTripTitle = PreSetTripTitle(
                    requireNotNull(tripTitle.value),
                    TripStatus.FINISHED,
                ),
            ).onSuccess {
                _titleSetupCompletedEvent.value = Event(true)
            }.onFailure {
                // todo 오류 처리
                TripDrawApplication.logUtil.general.log(it)
            }
        }
    }

    fun getTripPreviewInfo() {
        viewModelScope.launch {
            repository.getTrip(tripId)
                .onSuccess {
                    _previewTrip.value = it.toPreviewPresentation().toDomain()
                }
        }
    }
}
