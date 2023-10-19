package com.teamtripdraw.android.ui.filter.address

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamtripdraw.android.domain.repository.AddressRepository
import com.teamtripdraw.android.support.framework.presentation.event.Event
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

    private val _siDos: MutableLiveData<List<String>> = MutableLiveData(listOf())
    val siDos: LiveData<List<String>> = _siDos

    private val _siGunGus: MutableLiveData<List<String>> = MutableLiveData(listOf())
    val siGunGus: LiveData<List<String>> = _siGunGus

    private val _eupMyeonDongs: MutableLiveData<List<String>> = MutableLiveData(listOf())
    val eupMyeonDongs: LiveData<List<String>> = _eupMyeonDongs

    private val _selectingCompletedEvent: MutableLiveData<Event<Boolean>> =
        MutableLiveData(Event(false))
    val selectingCompletedEvent: LiveData<Event<Boolean>> = _selectingCompletedEvent

    init {
        fetchSiDos()
    }

    fun selectSiDo(siDo: String) {
        selectedSiDo.value = siDo
        selectedSiGunGu.value = ""
        selectedEupMyeonDong.value = ""

        _siGunGus.value = listOf()
        _eupMyeonDongs.value = listOf()

        fetchSiGunGu()
    }

    fun selectSiGunGu(siGunGu: String) {
        selectedSiGunGu.value = siGunGu
        selectedEupMyeonDong.value = ""

        _eupMyeonDongs.value = listOf()

        fetchEupMyeonDong()
    }

    fun selectEupMyeonDong(eupMyeonDong: String) {
        selectedEupMyeonDong.value = eupMyeonDong
    }

    private fun fetchSiDos() {
        viewModelScope.launch {
            val addresses: List<String> = addressRepository.getAddresses("", "")
            _siDos.value = addresses
        }
    }

    fun fetchSiGunGu() {
        viewModelScope.launch {
            val addresses = addressRepository.getAddresses(selectedSiDo.value!!, "")
            _siGunGus.value = addresses
        }
    }

    fun fetchEupMyeonDong() {
        viewModelScope.launch {
            val addresses =
                addressRepository.getAddresses(selectedSiDo.value!!, selectedSiGunGu.value!!)
            _eupMyeonDongs.value = addresses
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
