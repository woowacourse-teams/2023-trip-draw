package com.teamtripdraw.android.ui.postWriting

import android.content.Context
import android.content.Intent
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.teamtripdraw.android.R
import com.teamtripdraw.android.databinding.ActivityPostWritingBinding
import com.teamtripdraw.android.domain.constants.NULL_SUBSTITUTE_POINT_ID
import com.teamtripdraw.android.support.framework.presentation.extensions.getAdministrativeAddress
import com.teamtripdraw.android.support.framework.presentation.extensions.toFile
import com.teamtripdraw.android.ui.common.tripDrawViewModelFactory
import java.util.Locale

class PostWritingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostWritingBinding
    private val viewModel: PostWritingViewModel by viewModels { tripDrawViewModelFactory }

    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri == null) return@registerForActivityResult
            uri.toFile(this)?.let { viewModel.updateImage(it) }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_post_writing)

        initView()
        initTripData()
        initEvent()
    }

    private fun initView() {
        binding.lifecycleOwner = this
        binding.postWritingViewModel = viewModel

        binding.btnCamera.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun initTripData() {
        val pointId = intent.getLongExtra(INTENT_KEY_POINT_ID, NULL_SUBSTITUTE_POINT_ID)

        if (pointId == NULL_SUBSTITUTE_POINT_ID)
            throw IllegalArgumentException(WRONG_INTENT_VALUE_MESSAGE)

        viewModel.initTripData(pointId)
    }

    private fun initEvent() {
        viewModel.latLngPoint.observe(this) {
            val geocoder = Geocoder(this, Locale.KOREAN)
            viewModel.updateAddress(geocoder.getAdministrativeAddress(it.latitude, it.longitude))
        }

        viewModel.writingCompletedEvent.observe(this) {
            if (it == true) finish()
        }
    }

    companion object {
        private const val INTENT_KEY_POINT_ID = "pointId"
        private const val WRONG_INTENT_VALUE_MESSAGE = "인텐트 값이 잘못되었습니다."

        fun getIntent(context: Context, pointId: Long): Intent {
            val intent = Intent(context, PostWritingActivity::class.java)
            intent.putExtra(INTENT_KEY_POINT_ID, pointId)
            return intent
        }
    }
}