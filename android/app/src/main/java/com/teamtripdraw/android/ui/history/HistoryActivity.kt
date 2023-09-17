package com.teamtripdraw.android.ui.history

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.teamtripdraw.android.R
import com.teamtripdraw.android.databinding.ActivityMyHistoryBinding
import com.teamtripdraw.android.support.framework.presentation.event.EventObserver
import com.teamtripdraw.android.ui.common.tripDrawViewModelFactory
import com.teamtripdraw.android.ui.history.detail.HistoryDetailActivity
import com.teamtripdraw.android.ui.model.UiPreviewTrip

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyHistoryBinding

    private lateinit var historyAdapter: HistoryAdapter
    private val viewModel: HistoryViewModel by viewModels { tripDrawViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_history)

        binding.lifecycleOwner = this
        binding.historyViewModel = viewModel

        bindViewModel()
        setAdapter()
        initObserve()
    }

    private fun bindViewModel() {
        binding.historyViewModel = viewModel
    }

    private fun setAdapter() {
        historyAdapter = HistoryAdapter(viewModel)
        binding.rvTripHistory.adapter = historyAdapter
    }

    private fun initObserve() {
        initPreviewTripsObserve()
        initHistoryOpenObserveEvent()
        initBackPageObserve()
    }

    private fun initPreviewTripsObserve() {
        viewModel.previewTrips.observe(this) {
            historyAdapter.submitList(it.previewTrips)
        }
    }

    private fun initHistoryOpenObserveEvent() {
        viewModel.previewTripOpenEvent.observe(
            this,
            EventObserver(this@HistoryActivity::onHistoryClick),
        )
    }

    private fun onHistoryClick(previewTrip: UiPreviewTrip) {
        val intent = HistoryDetailActivity.getIntent(this, previewTrip)
        startActivity(intent)
    }

    private fun initBackPageObserve() {
        viewModel.backPageEvent.observe(
            this,
            EventObserver { if (it) finish() },
        )
    }

    override fun onStart() {
        super.onStart()
        fetchPreviewTrips()
    }

    private fun fetchPreviewTrips() = viewModel.fetchPreviewTrips()

    companion object {

        fun getIntent(context: Context): Intent {
            return Intent(context, HistoryActivity::class.java)
        }
    }
}
