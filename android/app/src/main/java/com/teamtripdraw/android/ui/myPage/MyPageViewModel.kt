package com.teamtripdraw.android.ui.myPage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamtripdraw.android.BuildConfig
import com.teamtripdraw.android.support.framework.presentation.event.Event
import kotlinx.coroutines.launch

class MyPageViewModel() : ViewModel() {

    val VERSION_NAME: String = BuildConfig.VERSION_NAME

    private val _nickname: MutableLiveData<String> = MutableLiveData("")
    val nickname: LiveData<String> = _nickname

    private val _openPrivacyPolicyEvent: MutableLiveData<Event<Boolean>> =
        MutableLiveData(Event(false))
    val openPrivacyPolicyEvent: LiveData<Event<Boolean>> = _openPrivacyPolicyEvent

    private val _signOutEvent: MutableLiveData<Event<Boolean>> = MutableLiveData(Event(false))
    val signOutEvent: LiveData<Event<Boolean>> = _signOutEvent

    private val _openAccountDeletionEvent: MutableLiveData<Event<Boolean>> =
        MutableLiveData(Event(false))
    val openAccountDeletionEvent: LiveData<Event<Boolean>> = _openAccountDeletionEvent

    fun fetchNickname() {
        viewModelScope.launch {
            // todo : _nickname.value = repositosy에서 가져온 닉네임
        }
    }

    fun signOut() {
        viewModelScope.launch {
            // todo : repositosy에서 로그아웃 진행
            _signOutEvent.value = Event(true)
        }
    }

    fun openPrivacyPolicy() {
        _openPrivacyPolicyEvent.value = Event(true)
        _openPrivacyPolicyEvent.value = Event(false)
    }

    fun openAccountDeletion() {
        _openAccountDeletionEvent.value = Event(true)
        _openAccountDeletionEvent.value = Event(false)
    }
}
