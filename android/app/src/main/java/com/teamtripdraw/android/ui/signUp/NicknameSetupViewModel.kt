package com.teamtripdraw.android.ui.signUp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamtripdraw.android.domain.exception.nicknameSetup.DuplicateNickNameException
import com.teamtripdraw.android.domain.exception.nicknameSetup.InvalidNickNameException
import com.teamtripdraw.android.domain.repository.NicknameSetupRepository
import com.teamtripdraw.android.domain.user.NicknameValidState
import com.teamtripdraw.android.ui.common.Event
import kotlinx.coroutines.launch

class NicknameSetupViewModel(
    private val nicknameSetupRepository: NicknameSetupRepository
) : ViewModel() {

    val MAX_INPUT_NAME_LENGTH = NicknameValidState.MAX_NAME_LENGTH + 1

    val nickname: MutableLiveData<String> = MutableLiveData("")

    private val _nicknameState: MutableLiveData<NicknameValidState> =
        MutableLiveData(NicknameValidState.EMPTY)
    val nicknameState: LiveData<NicknameValidState> = _nicknameState

    private val _nicknameSetupCompletedEvent = MutableLiveData<Event<Boolean>>()
    val nicknameSetupCompleteEvent: LiveData<Event<Boolean>>
        get() = _nicknameSetupCompletedEvent

    fun textChangedEvent() {
        _nicknameState.value = NicknameValidState.getValidState(requireNotNull(nickname.value))
    }

    suspend fun onNicknameSetupComplete() {
        viewModelScope.launch {
            nicknameSetupRepository.setNickName(requireNotNull(nickname.value))
                .onSuccess {
                    _nicknameSetupCompletedEvent.value = Event(true)
                }
                .onFailure {
                    when (it) {
                        is DuplicateNickNameException ->
                            _nicknameState.value = NicknameValidState.DUPLICATE
                        is InvalidNickNameException ->
                            _nicknameState.value = NicknameValidState.EMPTY
                    }
                }
        }
    }
}
