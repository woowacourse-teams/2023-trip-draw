package com.teamtripdraw.android.ui.post.writing

import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.teamtripdraw.android.R
import com.teamtripdraw.android.databinding.ActivityPostWritingBinding
import com.teamtripdraw.android.domain.model.point.Point
import com.teamtripdraw.android.domain.model.post.Post
import com.teamtripdraw.android.support.framework.presentation.event.EventObserver
import com.teamtripdraw.android.support.framework.presentation.extensions.fetchAddress
import com.teamtripdraw.android.support.framework.presentation.extensions.toResizedImageFile
import com.teamtripdraw.android.support.framework.presentation.images.createImageUri
import com.teamtripdraw.android.support.framework.presentation.permission.checkCameraPermission
import com.teamtripdraw.android.support.framework.presentation.permission.requestCameraPermission
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

    private val permissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions.count { it.value.not() } != 0) {
                // todo dialog로 변경 필요
                Toast.makeText(this, "권한을 허용해주세요", Toast.LENGTH_SHORT).show()
            }
        }

    private var imageUri: Uri? = null

    private val takePicture =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                imageUri?.toResizedImageFile(this)?.let {
                    viewModel.updateImage(it)
                }
            }
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
    }

    private fun initIntentData() {
        val pointId = intent.getLongExtra(INTENT_KEY_POINT_ID, Point.NULL_SUBSTITUTE_ID)
        val postId = intent.getLongExtra(INTENT_KEY_POST_ID, Post.NULL_SUBSTITUTE_ID)

        when {
            pointId != Point.NULL_SUBSTITUTE_ID && postId == Post.NULL_SUBSTITUTE_ID -> {
                viewModel.initWritingMode(WritingMode.NEW, pointId)
            }
            postId != Post.NULL_SUBSTITUTE_ID && pointId == Point.NULL_SUBSTITUTE_ID -> {
                viewModel.initWritingMode(WritingMode.EDIT, postId)
            }
            else -> throw IllegalArgumentException(WRONG_INTENT_VALUE_MESSAGE)
        }
    }

    private fun initObserve() {
        initBackPageObserve()
        initWritingCompletedObserve()
        initPointObserve()
        initTakePictureObserve()
        initSelectPhotoObserve()
    }

    private fun initBackPageObserve() {
        viewModel.backPageEvent.observe(
            this,
            EventObserver { if (it) finish() },
        )
    }

    private fun initWritingCompletedObserve() {
        viewModel.writingCompletedEvent.observe(
            this,
            EventObserver { if (it) finish() },
        )
    }

    private fun initPointObserve() {
        viewModel.point.observe(this) { point ->
            val geocoder = Geocoder(this, Locale.KOREAN)
            geocoder.fetchAddress(point.latitude, point.longitude, viewModel::updateAddress)
        }
    }

    private fun initTakePictureObserve() {
        viewModel.takePictureEvent.observe(this) {
            if (it.not()) return@observe
            if (checkCameraPermission(this)) {
                imageUri = createImageUri(contentResolver)
                takePicture.launch(imageUri)
            } else {
                requestCameraPermission(this, permissionRequest)
            }
        }
    }

    private fun initSelectPhotoObserve() {
        viewModel.selectPhotoEvent.observe(this) {
            if (it.not()) return@observe
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    companion object {
        private const val INTENT_KEY_POINT_ID = "pointId"
        private const val INTENT_KEY_POST_ID = "postId"
        private const val WRONG_INTENT_VALUE_MESSAGE = "인텐트 값이 잘못되었습니다. (PostWritingActivity)"

        /**
         * 새로운 글을 작성합니다.
         * 기존의 글을 수정하고 싶다면 "WritingMode.Edit"를 설정해주세요.
         */
        fun getIntent(context: Context, pointId: Long): Intent {
            val intent = Intent(context, PostWritingActivity::class.java)
            intent.putExtra(INTENT_KEY_POINT_ID, pointId)
            return intent
        }

        /**
         * 기존의 글을 수정합니다.
         * 새로운 글을 작성하고 싶다면 글 작성 모드를 설정하지 마세요.
         */
        fun getIntent(
            context: Context,
            postId: Long,
            wringMode: WritingMode,
        ): Intent {
            if (wringMode == WritingMode.NEW) {
                throw IllegalArgumentException(WRONG_INTENT_VALUE_MESSAGE)
            }
            val intent = Intent(context, PostWritingActivity::class.java)
            intent.putExtra(INTENT_KEY_POST_ID, postId)
            return intent
        }
    }
}
