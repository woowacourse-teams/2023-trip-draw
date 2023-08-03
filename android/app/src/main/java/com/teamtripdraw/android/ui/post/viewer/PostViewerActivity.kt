package com.teamtripdraw.android.ui.post.viewer

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.teamtripdraw.android.R
import com.teamtripdraw.android.databinding.ActivityPostViewerBinding
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

        initObserve()
        setUpView()
    }

    private fun initObserve() {
        viewModel.posts.observe(this) {
            adapter.submitList(it)
        }

        viewModel.openPostDetailEvent.observe(
            this,
            EventObserver(this@PostViewerActivity::onPostClick)
        )

        viewModel.postErrorEvent.observe(
            this,
            EventObserver(this@PostViewerActivity::onErrorOccurred)
        )
    }

    private fun setUpView() {
        adapter = PostViewerAdapter(viewModel)
        binding.rvPostViewer.adapter = adapter
        binding.onBackClick = { finish() }
        viewModel.getPosts()
    }

    private fun onPostClick(postId: Long) {
        startActivity(PostDetailActivity.getIntent(this, postId))
    }

    private fun onErrorOccurred(isErrorOccurred: Boolean) {
        if (isErrorOccurred) {
            Log.e("POST_VIEWER_ERROR", POST_VIEWER_ERROR)
            Snackbar.make(
                binding.root,
                R.string.post_viewer_error_description,
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    companion object {
        private const val POST_VIEWER_ERROR = "감상 목록을 불러오는데 오류가 발생했습니다."

        fun getIntent(context: Context): Intent {
            return Intent(context, PostViewerActivity::class.java)
        }
    }
}
