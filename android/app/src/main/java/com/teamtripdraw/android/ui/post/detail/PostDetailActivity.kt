package com.teamtripdraw.android.ui.post.detail

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.teamtripdraw.android.R
import com.teamtripdraw.android.databinding.ActivityPostDetailBinding
import com.teamtripdraw.android.domain.constants.NULL_SUBSTITUTE_POST_ID
import com.teamtripdraw.android.support.framework.presentation.event.EventObserver
import com.teamtripdraw.android.ui.common.tripDrawViewModelFactory

class PostDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostDetailBinding
    private val viewModel: PostDetailViewModel by viewModels { tripDrawViewModelFactory }
    private val postId by lazy { intent.getLongExtra(POST_ID_KEY, NULL_SUBSTITUTE_POST_ID) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_post_detail)

        binding.lifecycleOwner = this
        binding.postDetailViewModel = viewModel

        setUpView()
        initObserve()
    }

    private fun initObserve() {
        viewModel.postDeletedEvent.observe(
            this,
            EventObserver { finish() }
        )
    }

    private fun setUpView() {
        viewModel.postId.value = postId
        viewModel.getPost()
        binding.onBackClick = { finish() }
    }

    companion object {
        private const val POST_ID_KEY = "POST_ID"

        fun getIntent(context: Context, postId: Long): Intent {
            val intent = Intent(context, PostDetailActivity::class.java)
            intent.putExtra(POST_ID_KEY, postId)
            return intent
        }
    }
}
