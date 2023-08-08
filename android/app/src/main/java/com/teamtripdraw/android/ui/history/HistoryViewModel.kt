package com.teamtripdraw.android.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.teamtripdraw.android.support.framework.presentation.event.Event
import com.teamtripdraw.android.ui.model.UiTripItem

class HistoryViewModel : ViewModel() {

    private val _tripHistory: MutableLiveData<List<UiTripItem>> = MutableLiveData()
    val tripHistory: LiveData<List<UiTripItem>> = _tripHistory

    private val _historyOpenEvent = MutableLiveData<Event<UiTripItem>>()
    val historyOpenEvent: LiveData<Event<UiTripItem>> = _historyOpenEvent

    fun openHistoryDetail(historyItem: UiTripItem) {
        _historyOpenEvent.value = Event(historyItem)
    }
}
