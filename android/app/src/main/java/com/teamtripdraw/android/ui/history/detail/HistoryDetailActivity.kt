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
import com.teamtripdraw.android.ui.common.dialog.DialogUtil
import com.teamtripdraw.android.ui.common.dialog.SetTitleSituation
import com.teamtripdraw.android.ui.common.dialog.SetTripTitleDialog
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
        binding.historyDetailViewModel = viewModel

        getIntentData()
        setAdapter()
        setClickBack()
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
    }

    private fun setClickBack() {
        binding.onBackClick = { finish() }
    }

    private fun initObserve() {
        initPostObserve()
        initTripDetailEventObserve()
        initPostDetailEventObserve()
        initDeleteDialogEventObserve()
        initEditTripTitleObserve()
        initDeleteCompleteObserve()
    }

    private fun initPostObserve() {
        viewModel.post.observe(this) {
            adapter.submitList(it)
        }
    }

    private fun initTripDetailEventObserve() {
        viewModel.openTripDetailEvent.observe(
            this,
            EventObserver(this@HistoryDetailActivity::navigateToTripDetail),
        )
    }

    private fun navigateToTripDetail(tripId: Long) {
        val intent = TripDetailActivity.getIntent(this, tripId)
        startActivity(intent)
    }

    private fun initPostDetailEventObserve() {
        viewModel.openPostDetailEvent.observe(
            this,
            EventObserver(this@HistoryDetailActivity::navigateToPostDetail),
        )
    }

    private fun navigateToPostDetail(postId: Long) {
        val intent = PostDetailActivity.getIntent(this, postId)
        startActivity(intent)
    }

    private fun initDeleteDialogEventObserve() {
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

    private fun initEditTripTitleObserve() {
        viewModel.openEditTripTitleEvent.observe(this) {
            showEditTripTitleDialog()
        }
    }

    private fun showEditTripTitleDialog() {
        val setTripTitleDialog = SetTripTitleDialog()
        setTripTitleDialog.arguments =
            SetTripTitleDialog.getBundle(viewModel.tripId, SetTitleSituation.EDIT)
        setTripTitleDialog.show(supportFragmentManager, this.javaClass.name)
    }

    private fun initDeleteCompleteObserve() =
        viewModel.deleteCompleteEvent.observe(this) { if (it) finish() }

    override fun onResume() {
        super.onResume()
        fetchPosts()
    }

    private fun fetchPosts() = viewModel.fetchPosts()

    companion object {
        private const val TRIP_ITEM_KEY = "TRIP_ITEM_KEY"

        fun getIntent(context: Context, trip: UiPreviewTrip): Intent {
            val intent = Intent(context, HistoryDetailActivity::class.java)
            intent.putExtra(TRIP_ITEM_KEY, trip)
            return intent
        }
    }
}
