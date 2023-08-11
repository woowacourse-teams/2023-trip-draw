package com.teamtripdraw.android.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamtripdraw.android.domain.model.auth.LoginInfo
import com.teamtripdraw.android.domain.model.auth.LoginPlatform
import com.teamtripdraw.android.domain.repository.AuthRepository
import com.teamtripdraw.android.support.framework.presentation.event.Event
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _kakaoLoginEvent = MutableLiveData<Boolean>(false)
    val kakaoLoginEvent: LiveData<Boolean> = _kakaoLoginEvent

    private val _existedUserEvent = MutableLiveData<Event<LoginInfo>>()
    val existedUserEvent: LiveData<Event<LoginInfo>> = _existedUserEvent

    private val _newUserEvent = MutableLiveData<Event<LoginInfo>>()
    val newUserEvent: LiveData<Event<LoginInfo>> = _newUserEvent

    fun startKakaoLogin() {
        _kakaoLoginEvent.value = true
    }

    fun resetKakaoLoginEvent() {
        _kakaoLoginEvent.value = false
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
                    // todo 로그전략 수립후 로그 찍어주기
                }
        }
    }
}
