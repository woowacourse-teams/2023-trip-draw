package com.teamtripdraw.android.ui.home.markerSelectedBottomSheet

import androidx.lifecycle.ViewModel

abstract class MapBottomSheetViewModel : ViewModel() {

    abstract var markerSelectedState: Boolean
    abstract fun updateTripInfo()
}
