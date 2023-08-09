package com.teamtripdraw.android.ui.history.detail

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.teamtripdraw.android.R
import com.teamtripdraw.android.databinding.ActivityHistoryDetailBinding
import com.teamtripdraw.android.support.framework.presentation.getParcelableExtraCompat
import com.teamtripdraw.android.ui.common.bindingAdapter.setImage
import com.teamtripdraw.android.ui.common.tripDrawViewModelFactory
import com.teamtripdraw.android.ui.model.UiTrip

class HistoryDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryDetailBinding
    private val viewModel: HistoryDetailViewModel by viewModels { tripDrawViewModelFactory }
    private lateinit var adapter: HistoryDetailAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_history_detail)
        binding.lifecycleOwner = this

        setUpView()
        initObserve()
    }

    private fun initObserve() {
        viewModel.post.observe(this) {
            adapter.submitList(it)
        }

        viewModel.trip.observe(this) {
            binding.ivHistoryDetailImage.setImage(it.imageUrl)
            binding.ivHistoryDetailRoute.setImage(it.routeImageUrl)
            binding.tbHistoryDetail.title = it.name
        }
    }

    private fun setUpView() {
        val tripHistory = intent.getParcelableExtraCompat<UiTrip>(TRIP_ITEM_KEY)
            ?: throw java.lang.IllegalStateException()
        viewModel.updateTripItem(tripHistory)
        adapter = HistoryDetailAdapter(viewModel)
        binding.rvTripHistoryDetail.adapter = adapter
    }

    companion object {
        private const val TRIP_ITEM_KEY = "TRIP_ITEM_KEY"

        fun getIntent(context: Context, trip: UiTrip): Intent {
            val intent = Intent(context, HistoryDetailActivity::class.java)
            intent.putExtra(TRIP_ITEM_KEY, trip)
            return intent
        }
    }
}
