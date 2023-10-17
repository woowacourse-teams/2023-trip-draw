package com.teamtripdraw.android.ui.allTrips

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamtripdraw.android.TripDrawApplication
import com.teamtripdraw.android.domain.model.trip.TripOfAll
import com.teamtripdraw.android.domain.repository.TripRepository
import com.teamtripdraw.android.ui.filter.SelectedOptions
import com.teamtripdraw.android.ui.model.UiAllTrips
import com.teamtripdraw.android.ui.model.UiPreviewTrip
import com.teamtripdraw.android.ui.model.allTrips.UiAllTripItem
import com.teamtripdraw.android.ui.model.allTrips.UiAllTripLoadingItem
import com.teamtripdraw.android.ui.model.allTrips.UiAllTrips
import com.teamtripdraw.android.ui.model.allTrips.UiTripOfAll
import com.teamtripdraw.android.ui.model.mapper.toPresentation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllTripsViewModel @Inject constructor(
    private val tripRepository: TripRepository,
) : ViewModel() {

    private val _uiTripItems: MutableLiveData<List<UiAllTripItem>> = MutableLiveData(listOf())
    val trips: LiveData<UiAllTrips> = Transformations.map(_uiTripItems) { trip -> UiAllTrips(trip) }

    var selectedOptions: SelectedOptions? = null

    private val _openHistoryDetailEvent = MutableLiveData<UiPreviewTrip>()
    val openHistoryDetailEvent: LiveData<UiPreviewTrip> = _openHistoryDetailEvent

    private var lastId: Long? = null

    var hasNextPage = true
        private set

    var isAddLoading = false
        private set

    private val _openFilterSelectionEvent = MutableLiveData<Boolean>()
    val openFilterSelectionEvent: LiveData<Boolean> = _openFilterSelectionEvent

    fun fetchTrips() {
        if (_uiTripItems.value!!.isNotEmpty() && hasNextPage) {
            isAddLoading = true
            addLoadingItem()
        }
        fetchMoreTrips()
    }

    private fun fetchMoreTrips() {
        viewModelScope.launch {
            tripRepository.getAllTrips(lastViewedId = lastId).onSuccess { trips ->
                setLastItemId(trips)
                setHasNextPage(trips)
                setAddedItems(trips)
            }.onFailure { TripDrawApplication.logUtil.general.log(it) }
        }
    }

    private fun setAddedItems(trips: List<TripOfAll>) {
        _uiTripItems.value = _uiTripItems.value!!.toMutableList().apply {
            remove(UiAllTripLoadingItem)
            addAll(trips.map { it.toPresentation() })
        }
        isAddLoading = false
    }

    private fun addLoadingItem() {
        _uiTripItems.value =
            _uiTripItems.value!!.toMutableList().apply { add(UiAllTripLoadingItem) }
    }

    private fun setLastItemId(trips: List<TripOfAll>) {
        if (trips.isNotEmpty()) lastId = trips.last().tripId
    }

    private fun setHasNextPage(trips: List<TripOfAll>) {
        if (trips.size < PAGE_ITEM_SIZE && lastId != null) hasNextPage = false
    }

    fun updateSelectedOptions(options: SelectedOptions) {
        selectedOptions = options
    }

    fun openHistoryDetail(trip: UiTripOfAll) {
        val previewTrip = UiPreviewTrip(
            id = trip.tripId,
            name = trip.name,
            imageUrl = trip.imageUrl,
            routeImageUrl = trip.routeImageUrl,
        )
        _openHistoryDetailEvent.value = previewTrip
    }

    fun openFilterSelection() {
        _openFilterSelectionEvent.value = true
    }

    companion object {
        private const val PAGE_ITEM_SIZE = 20
    }
}
