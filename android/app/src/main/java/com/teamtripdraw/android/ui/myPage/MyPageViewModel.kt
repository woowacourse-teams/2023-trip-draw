package com.teamtripdraw.android.ui.myPage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamtripdraw.android.BuildConfig
import com.teamtripdraw.android.TripDrawApplication.DependencyContainer.logUtil
import com.teamtripdraw.android.domain.repository.AuthRepository
import com.teamtripdraw.android.domain.repository.TripRepository
import com.teamtripdraw.android.support.framework.presentation.event.Event
import kotlinx.coroutines.launch

class MyPageViewModel(
    private val authRepository: AuthRepository,
    private val tripRepository: TripRepository,
) : ViewModel() {

    val VERSION_NAME: String = BuildConfig.VERSION_NAME

    private val _nickname: MutableLiveData<String> = MutableLiveData("")
    val nickname: LiveData<String> = _nickname

    private val _openOpenSourceLicenseEvent: MutableLiveData<Boolean> = MutableLiveData(false)
    val openOpenSourceLicenseEvent: LiveData<Boolean> = _openOpenSourceLicenseEvent

    private val _openPrivacyPolicyEvent: MutableLiveData<Boolean> = MutableLiveData(false)
    val openPrivacyPolicyEvent: LiveData<Boolean> = _openPrivacyPolicyEvent

    private val _logoutEvent: MutableLiveData<Boolean> = MutableLiveData(false)
    val logoutEvent: LiveData<Boolean> = _logoutEvent

    private val _unsubscribeEvent: MutableLiveData<Boolean> = MutableLiveData(false)
    val unsubscribeEvent: LiveData<Boolean> = _unsubscribeEvent

    private val _unsubscribeSuccessEvent: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val unsubscribeSuccessEvent: LiveData<Event<Boolean>> = _unsubscribeSuccessEvent

    val currentTripId: Long get() = tripRepository.getCurrentTripId()

    fun fetchNickname() {
        viewModelScope.launch {
            viewModelScope.launch {
                authRepository.getUserInfo()
                    .onSuccess {
                        _nickname.value = it.nickname
                    }
                    .onFailure {
                        logUtil.general.log(it)
                    }
            }
        }
    }

    fun startLogoutEvent() {
        _logoutEvent.value = true
    }

    fun resetLogoutEvent() {
        _logoutEvent.value = false
    }

    fun openOpenSourceLicense() {
        _openOpenSourceLicenseEvent.value = true
        _openOpenSourceLicenseEvent.value = false
    }

    fun openPrivacyPolicy() {
        _openPrivacyPolicyEvent.value = true
        _openPrivacyPolicyEvent.value = false
    }

    fun startUnsubscribeEvent() {
        _unsubscribeEvent.value = true
    }

    fun resetUnsubscribeEvent() {
        _unsubscribeEvent.value = false
    }

    fun unsubscribe() {
        viewModelScope.launch {
            authRepository.unsubscribe()
                .onSuccess {
                    _unsubscribeSuccessEvent.value = Event(true)
                }
                .onFailure {
                    logUtil.general.log(it)
                }
        }
    }

    fun logout() {
        authRepository.logout()
    }

    fun clearCurrentTripId() {
        tripRepository.deleteCurrentTripId()
    }
}
