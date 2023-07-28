package com.teamtripdraw.android.ui.post.writing

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.teamtripdraw.android.R
import com.teamtripdraw.android.databinding.ActivityPostWritingBinding
import com.teamtripdraw.android.ui.common.tripDrawViewModelFactory

class PostWritingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostWritingBinding
    private val viewModel: PostWritingViewModel by viewModels { tripDrawViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_post_writing)

        binding.lifecycleOwner = this
        binding.postWritingViewModel = viewModel

    }

    companion object {
        private const val INTENT_KEY_TRIP_ID = "tripId"
        private const val INTENT_KEY_POINT_ID = "pointId"
        private const val INTENT_KEY_LATITUDE_ID = "latitude"
        private const val INTENT_KEY_LONGITUDE_ID = "longitude"

        fun getIntent(tripId: Long, pointId: Long, latitude: Double, longitude: Double): Intent {
            val intent = Intent()
            intent.putExtra(INTENT_KEY_TRIP_ID, tripId)
            intent.putExtra(INTENT_KEY_POINT_ID, pointId)
            intent.putExtra(INTENT_KEY_LATITUDE_ID, latitude)
            intent.putExtra(INTENT_KEY_LONGITUDE_ID, longitude)
            return intent
        }
    }
}