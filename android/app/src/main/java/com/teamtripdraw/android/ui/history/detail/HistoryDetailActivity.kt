package com.teamtripdraw.android.ui.history.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.teamtripdraw.android.R
import com.teamtripdraw.android.databinding.ActivityHistoryDetailBinding
import com.teamtripdraw.android.support.framework.presentation.event.EventObserver
import com.teamtripdraw.android.support.framework.presentation.getParcelableExtraCompat
import com.teamtripdraw.android.ui.common.bindingAdapter.setImage
import com.teamtripdraw.android.ui.common.dialog.DialogUtil
import com.teamtripdraw.android.ui.common.tripDrawViewModelFactory
import com.teamtripdraw.android.ui.history.tripDetail.TripDetailActivity
import com.teamtripdraw.android.ui.model.UiPreviewTrip
import com.teamtripdraw.android.ui.post.detail.PostDetailActivity

class HistoryDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryDetailBinding
    private val viewModel: HistoryDetailViewModel by viewModels { tripDrawViewModelFactory }
    private lateinit var adapter: HistoryDetailAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_history_detail)
        binding.lifecycleOwner = this

        getIntentData()
        setAdapter()
        setClickBack()
        setClickListener()
        initObserve()
    }

    private fun getIntentData() {
        val trip = intent.getParcelableExtraCompat<UiPreviewTrip>(TRIP_ITEM_KEY)
            ?: throw java.lang.IllegalStateException()
        viewModel.updatePreViewTrip(trip)
    }

    private fun setAdapter() {
        adapter = HistoryDetailAdapter(viewModel)
        binding.rvTripHistoryDetail.adapter = adapter
        viewModel.getPosts()
    }

    private fun setClickBack() {
        binding.onBackClick = { finish() }
    }

    private fun setClickListener() {
        // 이상하게 이 부분도 DataBinding이 되지 않아서 일단 이렇게 처리했습니다..
        binding.btnHistoryDetailDelete.setOnClickListener {
            viewModel.openDeleteDialog()
        }
    }

    private fun initObserve() {
        setPostObserve()
        setTripObserve()
        setTripDetailEventObserve()
        setPostDetailEventObserve()
        setDeleteDialogEventObserve()
        setDeleteCompleteObserve()
    }

    private fun setPostObserve() {
        viewModel.post.observe(this) {
            adapter.submitList(it)
        }
    }

    private fun setTripObserve() {
        viewModel.previewTrip.observe(this) {
            binding.ivHistoryDetailImage.setImage(it.imageUrl)
            binding.ivHistoryDetailRoute.setImage(it.routeImageUrl)
            binding.tbHistoryDetail.title = it.name
            binding.ivHistoryDetailImage.setOnClickListener {
                viewModel.openTripDetail()
            }
        }
    }

    private fun setTripDetailEventObserve() {
        viewModel.openTripDetailEvent.observe(
            this,
            EventObserver(this@HistoryDetailActivity::navigateToTripDetail),
        )
    }

    private fun navigateToTripDetail(tripId: Long) {
        val intent = TripDetailActivity.getIntent(this, tripId)
        startActivity(intent)
    }

    private fun setPostDetailEventObserve() {
        viewModel.openPostDetailEvent.observe(
            this,
            EventObserver(this@HistoryDetailActivity::navigateToPostDetail),
        )
    }

    private fun navigateToPostDetail(postId: Long) {
        val intent = PostDetailActivity.getIntent(this, postId)
        startActivity(intent)
    }

    private fun setDeleteDialogEventObserve() {
        viewModel.openDeleteDialogEvent.observe(
            this,
            EventObserver(this@HistoryDetailActivity::showDeleteDialog),
        )
    }

    private fun showDeleteDialog(isClick: Boolean) {
        if (isClick) {
            DialogUtil(DialogUtil.DELETE_CHECK) { viewModel.deleteTrip() }
                .show(supportFragmentManager, this.javaClass.name)
        }
    }

    private fun setDeleteCompleteObserve() =
        viewModel.deleteCompleteEvent.observe(this) { if (it) finish() }

    companion object {
        private const val TRIP_ITEM_KEY = "TRIP_ITEM_KEY"

        fun getIntent(context: Context, trip: UiPreviewTrip): Intent {
            val intent = Intent(context, HistoryDetailActivity::class.java)
            intent.putExtra(TRIP_ITEM_KEY, trip)
            return intent
        }
    }
}
