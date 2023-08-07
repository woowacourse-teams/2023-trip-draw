package com.teamtripdraw.android.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.teamtripdraw.android.support.framework.presentation.event.Event
import com.teamtripdraw.android.ui.model.UiHistory

class HistoryViewModel : ViewModel() {

    private val _tripHistory: MutableLiveData<List<UiHistory>> = MutableLiveData(
        listOf(
            UiHistory(
                id = 0,
                name = "경주 여행",
                imageUrl = "https://cdn.aitimes.kr/news/photo/202303/27617_41603_044.jpg",
                routeImageUrl = "https://cdn.aitimes.kr/news/photo/202303/27617_41603_044.jpg"
            ),
            UiHistory(
                id = 1,
                name = "오늘 주말을 잘 보내고 와서 기분이 좋다",
                imageUrl = "https://cdn.aitimes.kr/news/photo/202303/27617_41603_044.jpg",
                routeImageUrl = ""
            ),
            UiHistory(
                id = 2,
                name = "경주 여행2",
                imageUrl = "https://blog.kakaocdn.net/dn/0mySg/btqCUccOGVk/nQ68nZiNKoIEGNJkooELF1/img.jpg",
                routeImageUrl = ""
            ),
            UiHistory(
                id = 3,
                name = "경주 여행3",
                imageUrl = "",
                routeImageUrl = "https://cdn.aitimes.kr/news/photo/202303/27617_41603_044.jpg"
            ),
            UiHistory(
                id = 4,
                name = "경주 여행4",
                imageUrl = "",
                routeImageUrl = "https://blog.kakaocdn.net/dn/0mySg/btqCUccOGVk/nQ68nZiNKoIEGNJkooELF1/img.jpg"
            )
        )
    )
    val tripHistory: LiveData<List<UiHistory>> = _tripHistory

    private val _historyOpenEvent = MutableLiveData<Event<Long>>()
    val historyOpenEvent: LiveData<Event<Long>> = _historyOpenEvent

    fun openHistoryDetail(id: Long) {
        _historyOpenEvent.value = Event(id)
    }
}
