package com.teamtripdraw.android.ui.signUp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NicknameSetupViewModel : ViewModel() {

    val nickname: MutableLiveData<String> = MutableLiveData("")

    private val _nicknameState: MutableLiveData<NicknameState> = MutableLiveData(NicknameState.INITIAL)
    val nicknameState: LiveData<NicknameState> = _nicknameState

    fun updateNickNameState(nickname: String) {
        _nicknameState.value = NicknameState.getState(nickname)
    }
}
