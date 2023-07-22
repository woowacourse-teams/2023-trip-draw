package com.teamtripdraw.android.ui.signUp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.teamtripdraw.android.domain.repository.NicknameSetupRepository
import com.teamtripdraw.android.domain.user.NicknameValidState

class NicknameSetupViewModel(private val nicknameSetupRepository: NicknameSetupRepository) :
    ViewModel() {

    val MAX_INPUT_NAME_LENGTH = NicknameValidState.MAX_NAME_LENGTH + 1

    val nickname: MutableLiveData<String> = MutableLiveData("")

    private val _nicknameState: MutableLiveData<NicknameValidState> =
        MutableLiveData(NicknameValidState.EMPTY)
    val nicknameState: LiveData<NicknameValidState> = _nicknameState

    fun updateNickNameState(nickname: String) {
        _nicknameState.value = NicknameValidState.getValidState(nickname)
    }
}
