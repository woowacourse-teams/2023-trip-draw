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
import com.teamtripdraw.android.support.framework.presentation.event.EventObserver
import com.teamtripdraw.android.support.framework.presentation.extensions.fetchAddress
import com.teamtripdraw.android.ui.common.tripDrawViewModelFactory
import com.teamtripdraw.android.ui.home.HomeViewModel
import com.teamtripdraw.android.ui.postWriting.PostWritingActivity
import java.util.Locale

class MarkerSelectedBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetMarkerSelectedBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by viewModels({ requireParentFragment() })
    private val markerSelectedViewModel: MarkerSelectedViewModel by viewModels { tripDrawViewModelFactory }

    private lateinit var geocoder: Geocoder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initMarkerSelectedViewModelData()
        initGeocoder()
    }

    private fun initMarkerSelectedViewModelData() {
        initBundleData()?.let { pointId -> markerSelectedViewModel.updatePointId(pointId) }
        markerSelectedViewModel.getPointInfo()
    }

    private fun initBundleData(): Long? =
        arguments?.getLong(POINT_ID)

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
        initPostWritingEventObserver()
        initDeletePointEventObserver()
    }

    private fun getAddress() {
        markerSelectedViewModel.selectedUiPoint.observe(viewLifecycleOwner) { uiPoint ->
            geocoder.fetchAddress(uiPoint.latitude, uiPoint.longitude) { address ->
                markerSelectedViewModel.updateAddress(address)
            }
        }
    }

    private fun initPostWritingEventObserver() {
        markerSelectedViewModel.openPostWritingEvent.observe(
            viewLifecycleOwner,
            EventObserver(this::navigateToPostWriting)
        )
    }

    private fun navigateToPostWriting(pointId: Long) {
        startActivity(PostWritingActivity.getIntent(requireContext(), pointId))
        dismiss()
    }

    private fun initDeletePointEventObserver() {
        markerSelectedViewModel.deletePointEvent.observe(
            viewLifecycleOwner,
            EventObserver { if (it) dismiss() }
        )
    }

    override fun onDestroyView() {
        updateMapMarkers()
        super.onDestroyView()
    }

    private fun updateMapMarkers() {
        homeViewModel.updateCurrentTripRoute()
    }

    companion object {
        const val POINT_ID = "POINT_ID"

        fun getBundle(tripId: Long): Bundle =
            Bundle().apply { putLong(POINT_ID, tripId) }
    }
}
