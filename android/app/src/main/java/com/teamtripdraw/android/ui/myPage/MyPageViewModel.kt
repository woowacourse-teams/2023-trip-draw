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

    private val _openPrivacyPolicyEvent: MutableLiveData<Boolean> = MutableLiveData(false)
    val openPrivacyPolicyEvent: LiveData<Boolean> = _openPrivacyPolicyEvent

    private val _logoutEvent: MutableLiveData<Event<Boolean>> = MutableLiveData(Event(false))
    val logoutEvent: LiveData<Event<Boolean>> = _logoutEvent

    private val _openAccountDeletionEvent: MutableLiveData<Boolean> = MutableLiveData(false)
    val openAccountDeletionEvent: LiveData<Boolean> = _openAccountDeletionEvent

    fun fetchNickname() {
        viewModelScope.launch {
            // todo : _nickname.value = repositosy에서 가져온 닉네임
        }
    }

    fun logout() {
        viewModelScope.launch {
            // todo : repositosy에서 로그아웃 진행
            _logoutEvent.value = Event(true)
        }
    }

    fun openPrivacyPolicy() {
        _openPrivacyPolicyEvent.value = true
        _openPrivacyPolicyEvent.value = false
    }

    fun openAccountDeletion() {
        _openAccountDeletionEvent.value = true
        _openAccountDeletionEvent.value = false
    }
}
