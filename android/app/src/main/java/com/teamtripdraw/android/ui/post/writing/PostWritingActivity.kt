package com.teamtripdraw.android.ui.post.writing

import android.content.Context
import android.content.Intent
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
import com.teamtripdraw.android.domain.model.trip.Trip
import com.teamtripdraw.android.support.framework.presentation.event.EventObserver
import com.teamtripdraw.android.support.framework.presentation.extensions.toResizedImageFile
import com.teamtripdraw.android.support.framework.presentation.images.createImageUri
import com.teamtripdraw.android.support.framework.presentation.permission.checkCameraPermission
import com.teamtripdraw.android.support.framework.presentation.permission.requestCameraPermission
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostWritingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostWritingBinding
    private val viewModel: PostWritingViewModel by viewModels()

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
        val tripId = intent.getLongExtra(INTENT_KEY_TRIP_ID, Trip.NULL_SUBSTITUTE_ID)
        val pointId = intent.getLongExtra(INTENT_KEY_POINT_ID, Point.NULL_SUBSTITUTE_ID)
        val postId = intent.getLongExtra(INTENT_KEY_POST_ID, Post.NULL_SUBSTITUTE_ID)
        val latitude: Double = intent.getDoubleExtra(INTENT_KEY_LATITUDE, Point.NULL_LATITUDE)
        val longitude: Double = intent.getDoubleExtra(INTENT_KEY_LONGITUDE, Point.NULL_LONGITUDE)

        viewModel.initPostData(
            tripId = tripId,
            pointId = pointId,
            postId = postId,
            latitude = latitude,
            longitude = longitude,
        )
    }

    private fun initObserve() {
        initBackPageObserve()
        initWritingCompletedObserve()
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
        private const val INTENT_KEY_TRIP_ID = "tripId"
        private const val INTENT_KEY_POINT_ID = "pointId"
        private const val INTENT_KEY_POST_ID = "postId"
        private const val INTENT_KEY_LATITUDE = "latitude"
        private const val INTENT_KEY_LONGITUDE = "longitude"

        fun getIntentByPoint(context: Context, tripId: Long, pointId: Long): Intent {
            val intent = Intent(context, PostWritingActivity::class.java)
            intent.putExtra(INTENT_KEY_TRIP_ID, tripId)
            intent.putExtra(INTENT_KEY_POINT_ID, pointId)
            return intent
        }

        fun getIntentByLatLng(
            context: Context,
            tripId: Long,
            latitude: Double,
            longitude: Double,
        ): Intent {
            val intent = Intent(context, PostWritingActivity::class.java)
            intent.putExtra(INTENT_KEY_TRIP_ID, tripId)
            intent.putExtra(INTENT_KEY_LATITUDE, latitude)
            intent.putExtra(INTENT_KEY_LONGITUDE, longitude)
            return intent
        }

        fun getIntentByPost(
            context: Context,
            postId: Long,
        ): Intent {
            val intent = Intent(context, PostWritingActivity::class.java)
            intent.putExtra(INTENT_KEY_POST_ID, postId)
            return intent
        }
    }
}
