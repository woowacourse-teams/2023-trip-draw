package com.teamtripdraw.android.ui.signUp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.teamtripdraw.android.domain.model.user.NicknameValidState
import com.teamtripdraw.android.domain.repository.AuthRepository
import com.teamtripdraw.android.support.framework.presentation.event.Event

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

    fun nicknameChangedEvent() {
        _nicknameState.value = NicknameValidState.getValidState(requireNotNull(nickname.value))
    }

    fun setNickname() {
//        viewModelScope.launch {
//            authRepository.setNickname(requireNotNull(nickname.value))
//                .onSuccess {
//                    _nicknameSetupCompletedEvent.value = Event(true)
//                }
//                .onFailure {
//                    when (it) {
//                        is DuplicateNickNameException ->
//                            _nicknameState.value = NicknameValidState.DUPLICATE
//                    }
//                }.getOrNull()?.let {
//                    authRepository.getNickname(it)
//                }
//        }
    }
}
