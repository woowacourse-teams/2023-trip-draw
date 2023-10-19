package com.teamtripdraw.android.ui.filter.address

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
        _siDos.value?.let { siDoItems ->
            _siDos.value = siDoItems.toMutableList().map { item ->
                val isSelected = item.addressName == selectedSiDoItem.addressName
                UiAddressSelectionItem(item.addressName, isSelected)
            }
        }

        this.selectedSiDo.value = selectedSiDoItem.addressName
        selectedSiGunGu.value = ""
        selectedEupMyeonDong.value = ""

        _siGunGus.value = listOf()
        _eupMyeonDongs.value = listOf()

        fetchSiGunGu()
    }

    fun selectSiGunGu(selectedSiGunGuItem: UiAddressSelectionItem) {
        _siGunGus.value?.let { siGunGuItems ->
            _siGunGus.value = siGunGuItems.toMutableList().map { item ->
                val isSelected = item.addressName == selectedSiGunGuItem.addressName
                UiAddressSelectionItem(item.addressName, isSelected)
            }
        }

        selectedSiGunGu.value = selectedSiGunGuItem.addressName
        selectedEupMyeonDong.value = ""

        _eupMyeonDongs.value = listOf()

        fetchEupMyeonDong()
    }

    fun selectEupMyeonDong(selectedEupMyeonDongItem: UiAddressSelectionItem) {
        _eupMyeonDongs.value?.let { eupMyeonDongs ->
            _eupMyeonDongs.value = eupMyeonDongs.toMutableList().map { item ->
                val isSelected = item.addressName == selectedEupMyeonDongItem.addressName
                UiAddressSelectionItem(item.addressName, isSelected)
            }
        }

        selectedEupMyeonDong.value = selectedEupMyeonDongItem.addressName
    }

    private fun fetchSiDos() {
        viewModelScope.launch {
            val addresses: List<String> = addressRepository.getAddresses("", "")
            _siDos.value = addresses.map { UiAddressSelectionItem(it) }
        }
    }

    fun fetchSiGunGu() {
        viewModelScope.launch {
            val addresses: List<String> = addressRepository.getAddresses(selectedSiDo.value!!, "")
            _siGunGus.value = addresses.map { UiAddressSelectionItem(it) }
        }
    }

    fun fetchEupMyeonDong() {
        viewModelScope.launch {
            val addresses: List<String> =
                addressRepository.getAddresses(selectedSiDo.value!!, selectedSiGunGu.value!!)
            _eupMyeonDongs.value = addresses.map { UiAddressSelectionItem(it) }
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
