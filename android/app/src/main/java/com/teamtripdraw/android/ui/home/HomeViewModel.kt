package com.teamtripdraw.android.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.teamtripdraw.android.domain.repository.TripRepository

class HomeViewModel(
    private val tripRepository: TripRepository
) : ViewModel() {

    private val _homeUiState: MutableLiveData<HomeUiState> = MutableLiveData()
    val homeUiState: LiveData<HomeUiState> = _homeUiState

    init {
        // todo: Local의 trip Id 조사하여 없다면 여행전 업데이트 있다면 여행중 업데이트 로직
    }
}
