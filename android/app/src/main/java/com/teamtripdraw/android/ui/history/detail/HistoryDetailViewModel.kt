package com.teamtripdraw.android.ui.history.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.teamtripdraw.android.ui.model.UiHistoryItem
import com.teamtripdraw.android.ui.model.UiPostItem

class HistoryDetailViewModel : ViewModel() {

    private val _trip: MutableLiveData<UiHistoryItem> = MutableLiveData()
    val trip: LiveData<UiHistoryItem> = _trip

    private val _posts: MutableLiveData<List<UiPostItem>> = MutableLiveData()
    val post: LiveData<List<UiPostItem>> = _posts

    fun updateTripItem(tripItem: UiHistoryItem) {
        _trip.value = tripItem
    }
}
