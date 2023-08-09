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
import com.teamtripdraw.android.ui.model.UiTrip

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var historyAdapter: HistoryAdapter
    private val viewModel: HistoryViewModel by viewModels { tripDrawViewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner

        setUpView()
        initObserve()

        return binding.root
    }

    private fun setUpView() {
        historyAdapter = HistoryAdapter(viewModel)
        binding.rvTripHistory.adapter = historyAdapter
    }

    private fun initObserve() {
        viewModel.tripHistory.observe(viewLifecycleOwner) {
            historyAdapter.submitList(it)
        }

        viewModel.historyOpenEvent.observe(
            viewLifecycleOwner,
            EventObserver(this@HistoryFragment::onHistoryClick)
        )
    }

    private fun onHistoryClick(tripItem: UiTrip) {
        val intent = HistoryDetailActivity.getIntent(requireContext(), tripItem)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
