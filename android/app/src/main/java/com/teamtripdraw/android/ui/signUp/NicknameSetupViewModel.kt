package com.teamtripdraw.android.ui.signUp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamtripdraw.android.TripDrawApplication
import com.teamtripdraw.android.domain.exception.DuplicateNicknameException
import com.teamtripdraw.android.domain.model.auth.LoginInfo
import com.teamtripdraw.android.domain.model.user.NicknameValidState
import com.teamtripdraw.android.domain.repository.AuthRepository
import com.teamtripdraw.android.support.framework.presentation.event.Event
import com.teamtripdraw.android.ui.model.UiLoginInfo
import com.teamtripdraw.android.ui.model.mapper.toDomain
import kotlinx.coroutines.launch

class NicknameSetupViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {

    val MAX_INPUT_NAME_LENGTH = NicknameValidState.MAX_NAME_LENGTH + 1
    val MAX_NAME_LENGTH = NicknameValidState.MAX_NAME_LENGTH

    val nickname: MutableLiveData<String> = MutableLiveData("")

    private val _nicknameState: MutableLiveData<NicknameValidState> =
        MutableLiveData(NicknameValidState.EMPTY)
    val nicknameState: LiveData<NicknameValidState> = _nicknameState

    private val _nicknameSetupCompletedEvent = MutableLiveData<Event<Boolean>>()
    val nicknameSetupCompleteEvent: LiveData<Event<Boolean>> = _nicknameSetupCompletedEvent

    private lateinit var loginInfo: LoginInfo

    fun nicknameChangedEvent() {
        _nicknameState.value = NicknameValidState.getValidState(requireNotNull(nickname.value))
    }

    fun setNickname() {
        viewModelScope.launch {
            authRepository.setNickname(requireNotNull(nickname.value), loginInfo)
                .onSuccess {
                    _nicknameSetupCompletedEvent.value = Event(true)
                }.onFailure {
                    TripDrawApplication.logUtil.general.log(it, it.message)
                    when (it) {
                        is DuplicateNicknameException ->
                            _nicknameState.value = NicknameValidState.DUPLICATE
                    }
                }
        }
    }

    fun initLoginInfo(uiLoginInfo: UiLoginInfo) {
        loginInfo = uiLoginInfo.toDomain()
    }
}
