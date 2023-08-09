package com.teamtripdraw.android.ui.post.writing

import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.teamtripdraw.android.R
import com.teamtripdraw.android.databinding.ActivityPostWritingBinding
import com.teamtripdraw.android.domain.constants.NULL_SUBSTITUTE_POINT_ID
import com.teamtripdraw.android.domain.constants.NULL_SUBSTITUTE_POST_ID
import com.teamtripdraw.android.support.framework.presentation.extensions.fetchAddress
import com.teamtripdraw.android.support.framework.presentation.extensions.toResizedImageFile
import com.teamtripdraw.android.ui.common.tripDrawViewModelFactory
import java.util.Locale

class PostWritingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostWritingBinding
    private val viewModel: PostWritingViewModel by viewModels { tripDrawViewModelFactory }

    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri == null) return@registerForActivityResult
            uri.toResizedImageFile(this)?.let { viewModel.updateImage(it) }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_post_writing)

        initView()
        initIntentData()
        initObserve()
    }

    private fun initView() {
        binding.lifecycleOwner = this
        binding.postWritingViewModel = viewModel

        binding.onSelectPhoto =
            { pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }
        binding.onBackClick = { finish() }
    }

    private fun initIntentData() {
        val pointId = intent.getLongExtra(INTENT_KEY_POINT_ID, NULL_SUBSTITUTE_POINT_ID)
        val postId = intent.getLongExtra(INTENT_KEY_POST_ID, NULL_SUBSTITUTE_POST_ID)

        when {
            pointId != NULL_SUBSTITUTE_POINT_ID && postId == NULL_SUBSTITUTE_POST_ID -> {
                viewModel.initWritingMode(WritingMode.NEW, pointId)
            }
            postId != NULL_SUBSTITUTE_POST_ID && pointId == NULL_SUBSTITUTE_POINT_ID -> {
                viewModel.initWritingMode(WritingMode.EDIT, postId)
            }
            else -> throw IllegalArgumentException(WRONG_INTENT_VALUE_MESSAGE)
        }
    }

    private fun initObserve() {
        setPointObserveEvent()
        setWritingCompletedEvent()
    }

    private fun setPointObserveEvent() {
        viewModel.point.observe(this) { point ->
            val geocoder = Geocoder(this, Locale.KOREAN)
            geocoder.fetchAddress(point.latitude, point.longitude, viewModel::updateAddress)
        }
    }

    private fun setWritingCompletedEvent() {
        viewModel.writingCompletedEvent.observe(this) {
            if (it == true) finish()
        }
    }

    companion object {
        private const val INTENT_KEY_POINT_ID = "pointId"
        private const val INTENT_KEY_POST_ID = "postId"
        private const val WRONG_INTENT_VALUE_MESSAGE = "인텐트 값이 잘못되었습니다. (PostWritingActivity)"

        /**
         * Default is "New Post" mode.
         * If you want "Edit Post" change the mode to "WritingMode.Edit".
         */
        fun getIntent(context: Context, pointId: Long): Intent {
            val intent = Intent(context, PostWritingActivity::class.java)
            intent.putExtra(INTENT_KEY_POINT_ID, pointId)
            return intent
        }

        /**
         * Edit Post
         * If you want "New Post" don't choose mode.
         */
        fun getIntent(
            context: Context,
            postId: Long,
            wringMode: WritingMode,
        ): Intent {
            if (wringMode == WritingMode.NEW) {
                throw java.lang.IllegalArgumentException(WRONG_INTENT_VALUE_MESSAGE)
            }
            val intent = Intent(context, PostWritingActivity::class.java)
            intent.putExtra(INTENT_KEY_POST_ID, postId)
            return intent
        }
    }
}
