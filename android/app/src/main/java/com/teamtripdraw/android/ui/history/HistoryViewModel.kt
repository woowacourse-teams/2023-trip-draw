package com.teamtripdraw.android.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.teamtripdraw.android.support.framework.presentation.event.Event
import com.teamtripdraw.android.ui.model.UiPreviewTrip

class HistoryViewModel : ViewModel() {

    private val _tripHistory: MutableLiveData<List<UiPreviewTrip>> = MutableLiveData()
    val tripHistory: LiveData<List<UiPreviewTrip>> = _tripHistory

    private val _historyOpenEvent = MutableLiveData<Event<UiPreviewTrip>>()
    val historyOpenEvent: LiveData<Event<UiPreviewTrip>> = _historyOpenEvent

    fun openHistoryDetail(historyItem: UiPreviewTrip) {
        _historyOpenEvent.value = Event(historyItem)
    }
}
