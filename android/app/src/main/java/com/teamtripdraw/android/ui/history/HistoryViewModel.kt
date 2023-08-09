package com.teamtripdraw.android.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.teamtripdraw.android.support.framework.presentation.event.Event
import com.teamtripdraw.android.ui.model.UiTrip

class HistoryViewModel : ViewModel() {

    private val _tripHistory: MutableLiveData<List<UiTrip>> = MutableLiveData()
    val tripHistory: LiveData<List<UiTrip>> = _tripHistory

    private val _historyOpenEvent = MutableLiveData<Event<UiTrip>>()
    val historyOpenEvent: LiveData<Event<UiTrip>> = _historyOpenEvent

    fun openHistoryDetail(historyItem: UiTrip) {
        _historyOpenEvent.value = Event(historyItem)
    }
}
