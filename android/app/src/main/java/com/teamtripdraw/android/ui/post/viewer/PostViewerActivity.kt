package com.teamtripdraw.android.ui.post.viewer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.teamtripdraw.android.R
import com.teamtripdraw.android.databinding.ActivityPostViewerBinding
import com.teamtripdraw.android.domain.constants.NULL_SUBSTITUTE_TRIP_ID
import com.teamtripdraw.android.support.framework.presentation.event.EventObserver
import com.teamtripdraw.android.ui.common.tripDrawViewModelFactory
import com.teamtripdraw.android.ui.post.detail.PostDetailActivity

class PostViewerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostViewerBinding
    private val viewModel: PostViewerViewModel by viewModels { tripDrawViewModelFactory }
    private lateinit var adapter: PostViewerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_viewer)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_post_viewer)
        binding.lifecycleOwner = this

        bindViewModel()
        initTripId()
        initObserve()
        setAdapter()
        setClickBack()
    }

    private fun bindViewModel() {
        binding.postViewerViewModel = viewModel
    }

    private fun initTripId() {
        val tripId = intent.getLongExtra(TRIP_ID_KEY, NULL_SUBSTITUTE_TRIP_ID)
        viewModel.updateTripId(tripId)
    }

    private fun initObserve() {
        initPostsObserve()
        initOpenPostDetailEventObserve()
        initPostErrorEventObserve()
    }

    private fun initPostsObserve() {
        viewModel.posts.observe(this) {
            adapter.submitList(it)
        }
    }

    private fun initOpenPostDetailEventObserve() {
        viewModel.openPostDetailEvent.observe(
            this,
            EventObserver(this@PostViewerActivity::onPostClick),
        )
    }

    private fun onPostClick(postId: Long) {
        startActivity(PostDetailActivity.getIntent(this, postId))
    }

    private fun initPostErrorEventObserve() {
        viewModel.postErrorEvent.observe(
            this,
            EventObserver(this@PostViewerActivity::onErrorOccurred),
        )
    }

    private fun onErrorOccurred(isErrorOccurred: Boolean) {
        if (isErrorOccurred) {
            Log.e("POST_VIEWER_ERROR", POST_VIEWER_ERROR)
            Snackbar.make(
                binding.root,
                R.string.post_viewer_error_description,
                Snackbar.LENGTH_SHORT,
            ).show()
        }
    }

    private fun setAdapter() {
        adapter = PostViewerAdapter(viewModel)
        binding.rvPostViewer.adapter = adapter
    }

    private fun setClickBack() {
        binding.onBackClick = { finish() }
    }

    override fun onStart() {
        super.onStart()
        getPosts()
    }

    private fun getPosts() = viewModel.getPosts()

    companion object {
        private const val POST_VIEWER_ERROR = "감상 목록을 불러오는데 오류가 발생했습니다."
        private const val TRIP_ID_KEY = "TRIP_ID_KEY"

        fun getIntent(context: Context, tripId: Long): Intent {
            val intent = Intent(context, PostViewerActivity::class.java)
            intent.putExtra(TRIP_ID_KEY, tripId)
            return intent
        }
    }
}
