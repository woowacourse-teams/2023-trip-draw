package com.teamtripdraw.android.ui.myPage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamtripdraw.android.BuildConfig
import com.teamtripdraw.android.domain.repository.AuthRepository
import com.teamtripdraw.android.domain.repository.TripRepository
import kotlinx.coroutines.launch

class MyPageViewModel(
    private val authRepository: AuthRepository,
    private val tripRepository: TripRepository,
) : ViewModel() {

    val VERSION_NAME: String = BuildConfig.VERSION_NAME

    private val _nickname: MutableLiveData<String> = MutableLiveData("")
    val nickname: LiveData<String> = _nickname

    private val _openPrivacyPolicyEvent: MutableLiveData<Boolean> = MutableLiveData(false)
    val openPrivacyPolicyEvent: LiveData<Boolean> = _openPrivacyPolicyEvent

    private val _logoutEvent: MutableLiveData<Boolean> = MutableLiveData(false)
    val logoutEvent: LiveData<Boolean> = _logoutEvent

    private val _openAccountDeletionEvent: MutableLiveData<Boolean> = MutableLiveData(false)
    val openAccountDeletionEvent: LiveData<Boolean> = _openAccountDeletionEvent

    val currentTripId: Long get() = tripRepository.getCurrentTripId()

    fun fetchNickname() {
        viewModelScope.launch {
            // todo : _nickname.value = repositosy에서 가져온 닉네임
        }
    }

    fun startLogoutEvent() {
        _logoutEvent.value = true
    }

    fun openPrivacyPolicy() {
        _openPrivacyPolicyEvent.value = true
        _openPrivacyPolicyEvent.value = false
    }

    fun openAccountDeletion() {
        _openAccountDeletionEvent.value = true
        _openAccountDeletionEvent.value = false
    }

    fun logout() {
        authRepository.logout()
    }

    fun clearCurrentTripId() {
        tripRepository.deleteCurrentTripId()
    }
}
