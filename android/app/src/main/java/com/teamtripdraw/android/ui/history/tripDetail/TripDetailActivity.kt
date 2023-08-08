package com.teamtripdraw.android.ui.history.tripDetail

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import com.teamtripdraw.android.R
import com.teamtripdraw.android.databinding.ActivityTripDetailBinding
import com.teamtripdraw.android.domain.constants.NULL_SUBSTITUTE_TRIP_ID
import com.teamtripdraw.android.support.framework.presentation.Locations
import com.teamtripdraw.android.support.framework.presentation.event.EventObserver
import com.teamtripdraw.android.support.framework.presentation.naverMap.LOCATION_PERMISSION_REQUEST_CODE
import com.teamtripdraw.android.support.framework.presentation.naverMap.initUserInterface
import com.teamtripdraw.android.support.framework.presentation.naverMap.initUserLocationOption
import com.teamtripdraw.android.support.framework.presentation.resolution.toPixel
import com.teamtripdraw.android.ui.common.animation.ObjectAnimators
import com.teamtripdraw.android.ui.common.tripDrawViewModelFactory
import com.teamtripdraw.android.ui.post.viewer.PostViewerActivity
import com.teamtripdraw.android.ui.postWriting.PostWritingActivity

class TripDetailActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var naverMap: NaverMap
    private var fabState: Boolean = false

    private lateinit var binding: ActivityTripDetailBinding
    private val viewModel: TripDetailViewModel by viewModels { tripDrawViewModelFactory }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_trip_detail)
        binding.lifecycleOwner = this

        val tripId = intent.getLongExtra(TRIP_ID_KEY, NULL_SUBSTITUTE_TRIP_ID)
        viewModel.updateTripId(tripId)

        binding.tripDetailViewModel = viewModel
        binding.fabState = fabState

        initFloatingButtonClickListener()
        setUpPostViewerClickEvent()
        setUpPostWritingClickEvent()
        initPostWritingEventObserver()
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        binding.naverMap = this.naverMap
        this.naverMap.initUserInterface()
        this.naverMap.initUserLocationOption(
            FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE),
            this
        )
        this.naverMap.locationOverlay.icon = currentLocationImage
        this.naverMap.setContentPadding(0, 0, 0, toPixel(this, 67))
    }

    private fun initFloatingButtonClickListener() {
        binding.fabHome.setOnClickListener {
            ObjectAnimators.toggleFab(
                binding.fabHome,
                binding.fabWritePost,
                binding.fabPostList,
                binding.fabMarkerMode,
                binding.tvWritePost,
                binding.tvPostList,
                binding.tvMarkerMode,
                fabState
            )
            fabState = !fabState
            binding.fabState = fabState
        }
    }

    private fun setUpPostViewerClickEvent() {
        viewModel.openPostViewerEvent.observe(
            this,
            EventObserver(this@TripDetailActivity::navigateToPostViewer)
        )
    }

    private fun navigateToPostViewer(isClicked: Boolean) {
        if (isClicked) startActivity(PostViewerActivity.getIntent(this))    // todo: tripId 받아가기
    }

    private fun setUpPostWritingClickEvent() {
        binding.fabWritePost.setOnClickListener {
            Locations.getUpdateLocation(
                getFusedLocationClient(),
                this,
                viewModel::createPoint
            )
        }
    }

    private fun getFusedLocationClient(): FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(this)

    private fun initPostWritingEventObserver() {
        viewModel.openPostWritingEvent.observe(
            this,
            EventObserver(this::navigateToPostWriting)
        )
    }

    private fun navigateToPostWriting(pointId: Long) {
        startActivity(PostWritingActivity.getIntent(this, pointId))    // todo: tripId 받아가기
    }

    companion object {
        private val currentLocationImage = OverlayImage.fromResource(R.drawable.ic_current_location)
        private const val TRIP_ID_KEY = "TRIP_ID_KEY"

        fun getIntent(context: Context, tripId: Long): Intent {
            val intent = Intent(context, TripDetailActivity::class.java)
            intent.putExtra(TRIP_ID_KEY, tripId)
            return intent
        }
    }
}
