package com.teamtripdraw.android.ui.history.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.teamtripdraw.android.ui.model.UiPreviewTrip
import com.teamtripdraw.android.ui.model.UiPostItem

class HistoryDetailViewModel : ViewModel() {

    private val _previewTrip: MutableLiveData<UiPreviewTrip> = MutableLiveData()
    val previewTrip: LiveData<UiPreviewTrip> = _previewTrip

    private val _posts: MutableLiveData<List<UiPostItem>> = MutableLiveData()
    val post: LiveData<List<UiPostItem>> = _posts

    fun updatePreViewTrip(previewTrip: UiPreviewTrip) {
        _previewTrip.value = previewTrip
    }
}
