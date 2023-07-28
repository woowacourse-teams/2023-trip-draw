package com.teamtripdraw.android.ui.post.writing

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PostWritingViewModel : ViewModel() {

    val MAX_INPUT_TITLE_LENGTH = 100
    val MAX_INPUT_CONTENT_LENGTH = 10000

    val title: MutableLiveData<String> = MutableLiveData("")

    val content: MutableLiveData<String> = MutableLiveData("")

}