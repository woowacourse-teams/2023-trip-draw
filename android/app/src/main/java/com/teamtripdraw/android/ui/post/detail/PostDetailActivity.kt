package com.teamtripdraw.android.ui.post.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.teamtripdraw.android.R
import com.teamtripdraw.android.databinding.ActivityPostDetailBinding
import com.teamtripdraw.android.domain.model.post.Post
import com.teamtripdraw.android.support.framework.presentation.event.EventObserver
import com.teamtripdraw.android.ui.common.dialog.DialogUtil
import com.teamtripdraw.android.ui.post.writing.PostWritingActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostDetailBinding
    private val viewModel: PostDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_post_detail)

        binding.lifecycleOwner = this
        binding.postDetailViewModel = viewModel

        initIntentData()
        setUpView()
        initOpenDeleteDialogObserve()
        initDeleteCompleteObserve()
        initEditPostObserve()
    }

    override fun onStart() {
        super.onStart()
        viewModel.fetchPost()
    }

    private fun initIntentData() {
        val postId = intent.getLongExtra(POST_ID_KEY, Post.NULL_SUBSTITUTE_ID)
        if (postId == Post.NULL_SUBSTITUTE_ID) {
            throw IllegalArgumentException(WRONG_INTENT_VALUE_MESSAGE)
        }
        viewModel.initPostId(intent.getLongExtra(POST_ID_KEY, Post.NULL_SUBSTITUTE_ID))
    }

    private fun setUpView() {
        binding.onBackClick = { finish() }
    }

    private fun initOpenDeleteDialogObserve() {
        viewModel.openDeletionEvent.observe(
            this,
            EventObserver(this@PostDetailActivity::showDeleteDialog),
        )
    }

    private fun showDeleteDialog(isClick: Boolean) {
        if (isClick) {
            DialogUtil(DialogUtil.DELETE_CHECK) { viewModel.deletePost() }
                .show(supportFragmentManager, this.javaClass.name)
        }
    }

    private fun initDeleteCompleteObserve() =
        viewModel.postDeleteCompletedEvent.observe(this) { if (it) finish() }

    private fun initEditPostObserve() {
        viewModel.editEvent.observe(
            this,
            EventObserver {
                if (it) {
                    val intent = PostWritingActivity.getIntentByPost(this, viewModel.postId)
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
