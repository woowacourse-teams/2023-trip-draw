package com.teamtripdraw.android.ui.postWriting

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.teamtripdraw.android.R
import com.teamtripdraw.android.databinding.ActivityPostWritingBinding
import com.teamtripdraw.android.domain.constants.NULL_SUBSTITUTE_POINT_ID
import com.teamtripdraw.android.domain.constants.NULL_SUBSTITUTE_TRIP_ID
import com.teamtripdraw.android.ui.common.tripDrawViewModelFactory

class PostWritingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostWritingBinding
    private val viewModel: PostWritingViewModel by viewModels { tripDrawViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_post_writing)

        initView()
        initTripData()
    }

    private fun initView() {
        binding.lifecycleOwner = this
        binding.postWritingViewModel = viewModel
    }

    private fun initTripData() {
        val tripId: Long = intent.getLongExtra(INTENT_KEY_TRIP_ID, NULL_SUBSTITUTE_TRIP_ID)
        val pointId = intent.getLongExtra(INTENT_KEY_POINT_ID, NULL_SUBSTITUTE_POINT_ID)

        if (tripId == NULL_SUBSTITUTE_TRIP_ID || pointId == NULL_SUBSTITUTE_POINT_ID)
            throw IllegalArgumentException(WRONG_INTENT_VALUE_MESSAGE)

        viewModel.initTripData(tripId, pointId)
    }

    companion object {
        private const val INTENT_KEY_TRIP_ID = "tripId"
        private const val INTENT_KEY_POINT_ID = "pointId"
        private const val WRONG_INTENT_VALUE_MESSAGE = "인텐트 값이 잘못되었습니다."

        fun getIntent(context: Context, tripId: Long, pointId: Long): Intent {
            val intent = Intent(context, PostWritingActivity::class.java)
            intent.putExtra(INTENT_KEY_TRIP_ID, tripId)
            intent.putExtra(INTENT_KEY_POINT_ID, pointId)
            return intent
        }
    }
}
