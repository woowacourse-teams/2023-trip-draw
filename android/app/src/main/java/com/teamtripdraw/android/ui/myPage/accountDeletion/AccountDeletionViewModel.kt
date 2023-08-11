package com.teamtripdraw.android.ui.myPage.accountDeletion

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamtripdraw.android.support.framework.presentation.event.Event
import kotlinx.coroutines.launch

class AccountDeletionViewModel : ViewModel() {
    private val _backEvent: MutableLiveData<Event<Boolean>> = MutableLiveData(Event(false))
    val backEvent: LiveData<Event<Boolean>> = _backEvent

    private val _accountDeletionCompleteEvent: MutableLiveData<Event<Boolean>> =
        MutableLiveData(Event(false))
    val accountDeletionCompleteEvent: LiveData<Event<Boolean>> = _accountDeletionCompleteEvent

    fun backPage() {
        _backEvent.value = Event(true)
    }

    fun completeAccountDeletion() {
        viewModelScope.launch {
            // todo repository를 통해 회원 삭제
            _accountDeletionCompleteEvent.value = Event(true)
        }
    }
}
