package com.teamtripdraw.android.ui.history.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.teamtripdraw.android.ui.model.UiTripItem
import com.teamtripdraw.android.ui.model.UiPostItem

class HistoryDetailViewModel : ViewModel() {

    private val _trip: MutableLiveData<UiTripItem> = MutableLiveData()
    val trip: LiveData<UiTripItem> = _trip

    private val _posts: MutableLiveData<List<UiPostItem>> = MutableLiveData()
    val post: LiveData<List<UiPostItem>> = _posts

    fun updateTripItem(tripItem: UiTripItem) {
        _trip.value = tripItem
    }
}
