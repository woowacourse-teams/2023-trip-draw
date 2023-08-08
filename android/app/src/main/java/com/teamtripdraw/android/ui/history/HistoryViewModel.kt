package com.teamtripdraw.android.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.teamtripdraw.android.support.framework.presentation.event.Event
import com.teamtripdraw.android.ui.model.UiHistoryItem

class HistoryViewModel : ViewModel() {

    private val _tripHistory: MutableLiveData<List<UiHistoryItem>> = MutableLiveData()
    val tripHistory: LiveData<List<UiHistoryItem>> = _tripHistory

    private val _historyOpenEvent = MutableLiveData<Event<UiHistoryItem>>()
    val historyOpenEvent: LiveData<Event<UiHistoryItem>> = _historyOpenEvent

    fun openHistoryDetail(historyItem: UiHistoryItem) {
        _historyOpenEvent.value = Event(historyItem)
    }
}
