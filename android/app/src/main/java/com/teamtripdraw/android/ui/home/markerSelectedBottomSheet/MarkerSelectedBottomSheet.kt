package com.teamtripdraw.android.ui.home.markerSelectedBottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.teamtripdraw.android.R
import com.teamtripdraw.android.databinding.BottomSheetMarkerSelectedBinding
import com.teamtripdraw.android.support.framework.presentation.event.EventObserver
import com.teamtripdraw.android.support.framework.presentation.getParcelableCompat
import com.teamtripdraw.android.ui.history.tripDetail.TripDetailViewModel
import com.teamtripdraw.android.ui.home.HomeViewModel
import com.teamtripdraw.android.ui.post.writing.PostWritingActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MarkerSelectedBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetMarkerSelectedBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MapBottomSheetViewModel
    private val markerSelectedViewModel: MarkerSelectedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        updateMarkerSelectedStateToOpen()
        initMarkerSelectedViewModelData()
    }

    private fun initViewModel() {
        viewModel = when (initBottomSheetClickSituation()) {
            BottomSheetClickSituation.HOME -> {
                val homeViewModel: HomeViewModel by viewModels({ requireParentFragment() })
                homeViewModel
            }
            BottomSheetClickSituation.HISTORY -> {
                val tripDetailViewModel: TripDetailViewModel by activityViewModels()
                tripDetailViewModel
            }
        }
    }

    private fun initBottomSheetClickSituation(): BottomSheetClickSituation =
        arguments?.getParcelableCompat(BOTTOM_SHEET_CLICK_SITUATION_ID)
            ?: throw IllegalStateException()

    fun updateMarkerSelectedStateToOpen() {
        viewModel.markerSelectedState = true
    }

    private fun initMarkerSelectedViewModelData() {
        initPointData()?.let { pointId -> markerSelectedViewModel.updatePointId(pointId) }
        initTripId()?.let { tripId -> markerSelectedViewModel.updateTripId(tripId) }
        markerSelectedViewModel.getPointInfo()
    }

    private fun initPointData(): Long? = arguments?.getLong(POINT_ID)

    private fun initTripId(): Long? = arguments?.getLong(TRIP_ID)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = BottomSheetMarkerSelectedBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.markerSelectedViewModel = markerSelectedViewModel
        return binding.root
    }

    override fun getTheme(): Int = R.style.RoundBottomSheetStyleTheme

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPostWritingEventObserver()
        initDeletePointEventObserver()
    }

    private fun initPostWritingEventObserver() {
        markerSelectedViewModel.openPostWritingEvent.observe(
            viewLifecycleOwner,
            EventObserver(this::navigateToPostWriting),
        )
    }

    private fun navigateToPostWriting(pointId: Long) {
        val tripId = initTripId() ?: throw IllegalArgumentException("trip을 가져올 수 없습니다.")
        startActivity(PostWritingActivity.getIntent(requireContext(), tripId, pointId))
        dismiss()
    }

    private fun initDeletePointEventObserver() {
        markerSelectedViewModel.deletePointEvent.observe(
            viewLifecycleOwner,
            EventObserver { if (it) dismiss() },
        )
    }

    override fun onDestroyView() {
        updateMapMarkers()
        super.onDestroyView()
    }

    private fun updateMapMarkers() {
        viewModel.updateTripInfo()
    }

    override fun onDestroy() {
        updateMarkerSelectedStateToClose()
        _binding = null
        super.onDestroy()
    }

    fun updateMarkerSelectedStateToClose() {
        viewModel.markerSelectedState = false
    }

    companion object {
        private const val POINT_ID = "POINT_ID"
        private const val TRIP_ID = "TRIP_ID"
        private const val BOTTOM_SHEET_CLICK_SITUATION_ID = "BOTTOM_SHEET_CLICK_SITUATION_ID"

        fun getBundle(pointId: Long, tripId: Long, situation: BottomSheetClickSituation): Bundle {
            val bundle = Bundle()
            bundle.apply {
                putLong(POINT_ID, pointId)
                putLong(TRIP_ID, tripId)
                putParcelable(BOTTOM_SHEET_CLICK_SITUATION_ID, situation)
            }
            return bundle
        }
    }
}
