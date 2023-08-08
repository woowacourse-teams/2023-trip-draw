package com.teamtripdraw.android.ui.home.markerSelectedBottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.teamtripdraw.android.R
import com.teamtripdraw.android.databinding.BottomSheetMarkerSelectedBinding
import com.teamtripdraw.android.ui.home.HomeViewModel

class MarkerSelectedBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetMarkerSelectedBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by viewModels({ requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomSheetMarkerSelectedBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun getTheme(): Int = R.style.RoundBottomSheetStyleTheme

    companion object {
        private const val TRIP_ID = "TRIP_ID"

        fun getBundle(tripId: Long): Bundle =
            Bundle().apply { putLong(TRIP_ID, tripId) }
    }
}
