package com.teamtripdraw.android.ui.allTrips

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.teamtripdraw.android.databinding.FragmentAllTripsBinding
import com.teamtripdraw.android.ui.history.detail.HistoryDetailActivity
import com.teamtripdraw.android.ui.model.UiPreviewTrip
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllTripsFragment : Fragment() {

    private var _binding: FragmentAllTripsBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: AllTripsAdapter
    private val viewModel: AllTripsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAllTripsBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner

        bindViewModel()
        initObserver()
        setAdapter()
        addScrollListener()

        return binding.root
    }

    private fun bindViewModel() {
        binding.allTripsViewModel = viewModel
    }

    private fun initObserver() {
        initTripsObserve()
        initOpenPostDetailEventObserve()
    }

    private fun initTripsObserve() {
        viewModel.trips.observe(viewLifecycleOwner) {
            adapter.submitList(it.tripItems)
        }
    }

    private fun initOpenPostDetailEventObserve() {
        viewModel.openHistoryDetailEvent.observe(
            viewLifecycleOwner,
        ) { onTripClick(it) }
    }

    private fun onTripClick(trip: UiPreviewTrip) {
        startActivity(HistoryDetailActivity.getIntent(requireContext(), trip))
    }

    private fun setAdapter() {
        adapter = AllTripsAdapter(viewModel)
        binding.rvAllTrips.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        fetchTrips()
    }

    private fun fetchTrips() = viewModel.fetchTrips()

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun addScrollListener() {
        binding.rvAllTrips.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = binding.rvAllTrips.layoutManager as LinearLayoutManager
                val lastPosition = layoutManager.findLastCompletelyVisibleItemPosition()
                if (isMoreFetchTripsCondition(layoutManager, lastPosition)) viewModel.fetchTrips()
            }
        })
    }

    private fun isMoreFetchTripsCondition(layoutManager: LinearLayoutManager, lastPosition: Int):
        Boolean =
        viewModel.hasNextPage &&
            viewModel.isAddLoading.not() &&
            isLoadThreshold(layoutManager, lastPosition) &&
            binding.rvAllTrips.canScrollVertically(DOWNWARD_DIRECTION).not()

    private fun isLoadThreshold(layoutManager: LinearLayoutManager, lastPosition: Int): Boolean =
        layoutManager.itemCount <= lastPosition + LOAD_THRESHOLD

    companion object {
        private const val LOAD_THRESHOLD = 3
        private const val DOWNWARD_DIRECTION = 1
    }
}
