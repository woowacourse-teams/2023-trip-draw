package com.teamtripdraw.android.ui.home.markerSelectedBottomSheet

import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.teamtripdraw.android.R
import com.teamtripdraw.android.databinding.BottomSheetMarkerSelectedBinding
import com.teamtripdraw.android.support.framework.presentation.extensions.fetchAddress
import com.teamtripdraw.android.ui.common.tripDrawViewModelFactory
import com.teamtripdraw.android.ui.home.HomeViewModel
import java.util.Locale

class MarkerSelectedBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetMarkerSelectedBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by viewModels({ requireParentFragment() })
    private val markerSelectedViewModel: MarkerSelectedViewModel by viewModels { tripDrawViewModelFactory }

    private lateinit var geocoder: Geocoder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBundleData()?.let { pointId ->
            markerSelectedViewModel.updatePointId(pointId)
        }
        markerSelectedViewModel.getPointInfo()
        initGeocoder()
    }

    private fun initBundleData(): Long? =
        arguments?.getLong(TRIP_ID)

    private fun initGeocoder() {
        geocoder = Geocoder(requireContext(), Locale.KOREAN)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomSheetMarkerSelectedBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.markerSelectedViewModel = markerSelectedViewModel
        return binding.root
    }

    override fun getTheme(): Int = R.style.RoundBottomSheetStyleTheme

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getAddress()
    }

    private fun getAddress() {
        markerSelectedViewModel.selectedUiPoint.observe(viewLifecycleOwner) { uiPoint ->
            geocoder.fetchAddress(uiPoint.latitude, uiPoint.longitude) { address ->
                markerSelectedViewModel.updateAddress(address)
            }
        }
    }

    companion object {
        private const val TRIP_ID = "TRIP_ID"

        fun getBundle(tripId: Long): Bundle =
            Bundle().apply { putLong(TRIP_ID, tripId) }
    }
}
