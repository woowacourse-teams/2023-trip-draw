package com.teamtripdraw.android.ui.history.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.teamtripdraw.android.ui.model.UiTrip
import com.teamtripdraw.android.ui.model.UiPostItem

class HistoryDetailViewModel : ViewModel() {

    private val _trip: MutableLiveData<UiTrip> = MutableLiveData()
    val trip: LiveData<UiTrip> = _trip

    private val _posts: MutableLiveData<List<UiPostItem>> = MutableLiveData()
    val post: LiveData<List<UiPostItem>> = _posts

    fun updateTripItem(tripItem: UiTrip) {
        _trip.value = tripItem
    }
}
