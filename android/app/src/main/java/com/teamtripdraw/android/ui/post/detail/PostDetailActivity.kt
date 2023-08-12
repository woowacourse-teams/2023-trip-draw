package com.teamtripdraw.android.ui.post.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.teamtripdraw.android.R
import com.teamtripdraw.android.databinding.ActivityPostDetailBinding
import com.teamtripdraw.android.domain.constants.NULL_SUBSTITUTE_POST_ID
import com.teamtripdraw.android.support.framework.presentation.event.EventObserver
import com.teamtripdraw.android.ui.common.tripDrawViewModelFactory
import com.teamtripdraw.android.ui.post.writing.PostWritingActivity
import com.teamtripdraw.android.ui.post.writing.WritingMode

class PostDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostDetailBinding
    private val viewModel: PostDetailViewModel by viewModels { tripDrawViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_post_detail)

        binding.lifecycleOwner = this
        binding.postDetailViewModel = viewModel

        initIntentData()
        setUpView()
        initPostDeletedObserve()
        initEditPostObserve()
    }

    override fun onStart() {
        super.onStart()
        viewModel.fetchPost()
    }

    private fun initIntentData() {
        val postId = intent.getLongExtra(POST_ID_KEY, NULL_SUBSTITUTE_POST_ID)
        if (postId == NULL_SUBSTITUTE_POST_ID) {
            throw IllegalArgumentException(WRONG_INTENT_VALUE_MESSAGE)
        }
        viewModel.initPostId(intent.getLongExtra(POST_ID_KEY, NULL_SUBSTITUTE_POST_ID))
    }

    private fun setUpView() {
        binding.onBackClick = { finish() }
    }

    private fun initPostDeletedObserve() {
        viewModel.postDeletedEvent.observe(
            this,
            EventObserver { finish() },
        )
    }

    private fun initEditPostObserve() {
        viewModel.editEvent.observe(
            this,
            EventObserver {
                if (it) {
                    val intent =
                        PostWritingActivity.getIntent(this, viewModel.postId, WritingMode.EDIT)
                    startActivity(intent)
                }
            },
        )
    }

    companion object {
        private const val POST_ID_KEY = "POST_ID"
        private const val WRONG_INTENT_VALUE_MESSAGE = "인텐트 값이 잘못되었습니다. (PostDetailActivity)"

        fun getIntent(context: Context, postId: Long): Intent {
            val intent = Intent(context, PostDetailActivity::class.java)
            intent.putExtra(POST_ID_KEY, postId)
            return intent
        }
    }
}
