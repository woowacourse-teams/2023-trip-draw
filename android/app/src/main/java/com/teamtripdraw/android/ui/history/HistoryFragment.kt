package com.teamtripdraw.android.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.teamtripdraw.android.databinding.FragmentHistoryBinding
import com.teamtripdraw.android.support.framework.presentation.event.EventObserver
import com.teamtripdraw.android.ui.common.tripDrawViewModelFactory
import com.teamtripdraw.android.ui.history.detail.HistoryDetailActivity
import com.teamtripdraw.android.ui.model.UiPreviewTrip

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var historyAdapter: HistoryAdapter
    private val viewModel: HistoryViewModel by viewModels { tripDrawViewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner

        setAdapter()
        getPreviewTrips()
        initObserve()

        return binding.root
    }

    private fun setAdapter() {
        historyAdapter = HistoryAdapter(viewModel)
        binding.rvTripHistory.adapter = historyAdapter
    }

    private fun getPreviewTrips() = viewModel.getPreviewTrips()

    private fun initObserve() {
        initPreviewTripsObserve()
        initHistoryOpenObserveEvent()
    }

    private fun initPreviewTripsObserve() {
        viewModel.previewTrips.observe(viewLifecycleOwner) {
            historyAdapter.submitList(it)
        }
    }

    private fun initHistoryOpenObserveEvent() {
        viewModel.previewTripOpenEvent.observe(
            viewLifecycleOwner,
            EventObserver(this@HistoryFragment::onHistoryClick),
        )
    }

    private fun onHistoryClick(previewTrip: UiPreviewTrip) {
        val intent = HistoryDetailActivity.getIntent(requireContext(), previewTrip)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
