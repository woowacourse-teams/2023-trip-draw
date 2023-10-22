package com.teamtripdraw.android.ui.filter.address

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamtripdraw.android.TripDrawApplication
import com.teamtripdraw.android.domain.repository.AddressRepository
import com.teamtripdraw.android.support.framework.presentation.event.Event
import com.teamtripdraw.android.ui.model.UiAddressSelectionItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressSelectionViewModel @Inject constructor(
    private val addressRepository: AddressRepository,
) : ViewModel() {

    private val selectedSiDo: MutableLiveData<String> = MutableLiveData("")
    val selectedSiGunGu: MutableLiveData<String> = MutableLiveData("")
    private val selectedEupMyeonDong: MutableLiveData<String> = MutableLiveData("")
    val address: String get() = "${selectedSiDo.value} ${selectedSiGunGu.value} ${selectedEupMyeonDong.value}"

    private val _siDos: MutableLiveData<List<UiAddressSelectionItem>> = MutableLiveData(listOf())
    val siDos: LiveData<List<UiAddressSelectionItem>> = _siDos

    private val _siGunGus: MutableLiveData<List<UiAddressSelectionItem>> = MutableLiveData(listOf())
    val siGunGus: LiveData<List<UiAddressSelectionItem>> = _siGunGus

    private val _eupMyeonDongs: MutableLiveData<List<UiAddressSelectionItem>> =
        MutableLiveData(listOf())
    val eupMyeonDongs: LiveData<List<UiAddressSelectionItem>> = _eupMyeonDongs

    private val _selectingCompletedEvent: MutableLiveData<Event<Boolean>> =
        MutableLiveData(Event(false))
    val selectingCompletedEvent: LiveData<Event<Boolean>> = _selectingCompletedEvent

    init {
        fetchSiDos()
    }

    fun selectSiDo(selectedSiDoItem: UiAddressSelectionItem) {
        selectedSiDo.value = selectedSiDoItem.addressName
        _siDos.value?.let { siDoItems ->
            _siDos.value = getSelectedChangedList(siDoItems, selectedSiDoItem)
        }

        resetSiGunGu()
        resetEupMyeonDong()
        fetchSiGunGu()
    }

    fun selectSiGunGu(selectedSiGunGuItem: UiAddressSelectionItem) {
        selectedSiGunGu.value = selectedSiGunGuItem.addressName
        _siGunGus.value?.let { siGunGuItems ->
            _siGunGus.value = getSelectedChangedList(siGunGuItems, selectedSiGunGuItem)
        }

        resetEupMyeonDong()
        fetchEupMyeonDong()
    }

    fun selectEupMyeonDong(selectedEupMyeonDongItem: UiAddressSelectionItem) {
        selectedEupMyeonDong.value = selectedEupMyeonDongItem.addressName
        _eupMyeonDongs.value?.let { eupMyeonDongs ->
            _eupMyeonDongs.value = getSelectedChangedList(eupMyeonDongs, selectedEupMyeonDongItem)
        }
    }

    private fun getSelectedChangedList(
        list: List<UiAddressSelectionItem>,
        selectedItem: UiAddressSelectionItem,
    ): List<UiAddressSelectionItem> {
        return list.toMutableList().map { item ->
            val isSelected = item.addressName == selectedItem.addressName
            if (isSelected || item.isSelected) { // item의 selected가 true로 변경되거나 false로 변경되는 경우에만 새로운 인스턴스를 생성한다.
                UiAddressSelectionItem(item.addressName, isSelected)
            } else {
                item
            }
        }
    }

    private fun resetSiGunGu() {
        selectedSiGunGu.value = ""
        _siGunGus.value = listOf()
    }

    private fun resetEupMyeonDong() {
        selectedEupMyeonDong.value = ""
        _eupMyeonDongs.value = listOf()
    }

    private fun fetchSiDos() {
        viewModelScope.launch {
            addressRepository.getSiDos()
                .onSuccess { getSiDos ->
                    _siDos.value = getSiDos.map { UiAddressSelectionItem(it) }
                }.onFailure { TripDrawApplication.logUtil.general.log(it) }
        }
    }

    private fun fetchSiGunGu() {
        viewModelScope.launch {
            addressRepository.getSiGunGus(selectedSiDo.value!!)
                .onSuccess { getGunGus ->
                    _siGunGus.value = getGunGus.map { UiAddressSelectionItem(it) }
                }.onFailure { TripDrawApplication.logUtil.general.log(it) }
        }
    }

    private fun fetchEupMyeonDong() {
        viewModelScope.launch {
            addressRepository.getEupMyeonDongs(selectedSiDo.value!!, selectedSiGunGu.value!!)
                .onSuccess { getEupMyeonDongs ->
                    _eupMyeonDongs.value = getEupMyeonDongs.map { UiAddressSelectionItem(it) }
                }.onFailure { TripDrawApplication.logUtil.general.log(it) }
        }
    }

    fun completeSelecting() {
        if (selectedEupMyeonDong.value!!.isEmpty() ||
            selectedSiGunGu.value!!.isEmpty() ||
            selectedSiDo.value!!.isEmpty()
        ) {
            return
        }

        _selectingCompletedEvent.value = Event(true)
    }
}
