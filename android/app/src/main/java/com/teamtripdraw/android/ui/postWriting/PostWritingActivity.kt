package com.teamtripdraw.android.ui.postWriting

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.teamtripdraw.android.R
import com.teamtripdraw.android.databinding.ActivityPostWritingBinding
import com.teamtripdraw.android.domain.model.point.Point
import com.teamtripdraw.android.ui.common.tripDrawViewModelFactory
import com.teamtripdraw.android.ui.postWriting.model.UiPoint
import com.teamtripdraw.android.ui.postWriting.model.toDomain
import com.teamtripdraw.android.ui.postWriting.model.toPresentation
import java.time.LocalDateTime

class PostWritingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostWritingBinding
    private val viewModel: PostWritingViewModel by viewModels { tripDrawViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_post_writing)

        init()
    }

    private fun init() {
        binding.lifecycleOwner = this
        binding.postWritingViewModel = viewModel
        initData()
    }

    private fun initData() {
        val tripId: Long = intent.getLongExtra(INTENT_KEY_TRIP_ID, WRONG_ID)
        val uiPoint = intent.getSerializableExtra(INTENT_KEY_POINT) as UiPoint? // todo 버전 대응

        if (tripId == WRONG_ID || uiPoint == null)
            throw IllegalArgumentException(WRONG_INTENT_VALUE_MESSAGE)

        viewModel.initData(tripId, uiPoint.toDomain())
    }

    companion object {
        private const val INTENT_KEY_TRIP_ID = "tripId"
        private const val INTENT_KEY_POINT = "point"
        private const val WRONG_INTENT_VALUE_MESSAGE = "인텐트 값이 잘못되었습니다."
        private const val WRONG_ID: Long = -1

        fun getIntent(context: Context, tripId: Long = 1, point: Point = Point(1, 1.1, 1.1, LocalDateTime.now())): Intent {
            val intent = Intent(context, PostWritingActivity::class.java)
            intent.putExtra(INTENT_KEY_TRIP_ID, tripId)
            intent.putExtra(INTENT_KEY_POINT, point.toPresentation())
            return intent
        }
    }
}
