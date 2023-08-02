package com.teamtripdraw.android.ui.post.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.teamtripdraw.android.ui.model.UiPostDetail

class PostDetailViewModel : ViewModel() {

    private val _postDetail: MutableLiveData<UiPostDetail> = MutableLiveData()
    val postDetail: LiveData<UiPostDetail> = _postDetail
}
