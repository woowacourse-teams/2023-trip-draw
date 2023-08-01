package com.teamtripdraw.android.ui.post.viewer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
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

        viewModel.postClickedEvent.observe(
            this,
            EventObserver(this@PostViewerActivity::onPostClick)
        )
    }

    private fun setUpView() {
        adapter = PostViewerAdapter(viewModel)
        binding.rvPostViewer.adapter = adapter
        binding.onBackClick = { finish() }
        viewModel.getPosts()
    }

    private fun onPostClick(postId: Long) {
        // todo: PostDetailActivity 측에서 intent 받아오도록 수정
        val intent = Intent(this, PostDetailActivity::class.java)
        intent.putExtra("POST_ID", postId)
        startActivity(intent)
    }
}
