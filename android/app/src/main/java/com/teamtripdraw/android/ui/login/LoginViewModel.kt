package com.teamtripdraw.android.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamtripdraw.android.TripDrawApplication.DependencyContainer.logUtil
import com.teamtripdraw.android.domain.model.auth.LoginInfo
import com.teamtripdraw.android.domain.model.auth.LoginPlatform
import com.teamtripdraw.android.domain.repository.AuthRepository
import com.teamtripdraw.android.support.framework.presentation.event.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _kakaoLoginEvent = MutableLiveData<Boolean>(false)
    val kakaoLoginEvent: LiveData<Boolean> = _kakaoLoginEvent

    private val _openPrivacyPolicyEvent = MutableLiveData<Boolean>(false)
    val openPrivacyPolicyEvent: LiveData<Boolean> = _openPrivacyPolicyEvent

    private val _openTermsOfServiceEvent = MutableLiveData<Boolean>(false)
    val openTermsOfServiceEvent: LiveData<Boolean> = _openTermsOfServiceEvent

    private val _existedUserEvent = MutableLiveData<Event<LoginInfo>>()
    val existedUserEvent: LiveData<Event<LoginInfo>> = _existedUserEvent

    val existedUserEventValue: LoginInfo? get() = existedUserEvent.value?.content

    private val _newUserEvent = MutableLiveData<Event<LoginInfo>>()
    val newUserEvent: LiveData<Event<LoginInfo>> = _newUserEvent

    private val _nicknameExistsEvent = MutableLiveData<Event<Boolean>>()
    val nicknameExistsEvent: LiveData<Event<Boolean>> = _nicknameExistsEvent

    private val _autoLoginEvent = MutableLiveData<Event<Boolean>>()
    val autoLoginEvent: LiveData<Event<Boolean>> = _autoLoginEvent

    fun startKakaoLogin() {
        _kakaoLoginEvent.value = true
    }

    fun resetKakaoLoginEvent() {
        _kakaoLoginEvent.value = false
    }

    fun openPrivacyPolicy() {
        _openPrivacyPolicyEvent.value = true
        _openPrivacyPolicyEvent.value = false
    }

    fun openTermsOfService() {
        _openTermsOfServiceEvent.value = true
        _openTermsOfServiceEvent.value = false
    }

    fun login(platform: LoginPlatform, socialToken: String) {
        val loginInfo = LoginInfo(platform, socialToken)
        viewModelScope.launch {
            authRepository.login(loginInfo)
                .onSuccess {
                    if (it) {
                        _existedUserEvent.value = Event(loginInfo)
                    } else {
                        _newUserEvent.value = Event(loginInfo)
                    }
                }.onFailure {
                    logUtil.general.log(it)
                }
        }
    }

    fun fetchUserHasNickname() {
        viewModelScope.launch {
            authRepository.getUserInfo()
                .onSuccess {
                    _nicknameExistsEvent.value = Event(it.nickname.isNotBlank())
                }.onFailure {
                    logUtil.general.log(it)
                }
        }
    }

    fun fetchAutoLoginState() {
        _autoLoginEvent.value = Event(authRepository.getAutoLoginState())
    }
}
