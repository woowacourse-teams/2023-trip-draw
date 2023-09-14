package com.teamtripdraw.android.ui.history.tripDetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.OverlayImage
import com.teamtripdraw.android.R
import com.teamtripdraw.android.databinding.ActivityTripDetailBinding
import com.teamtripdraw.android.domain.model.trip.Trip
import com.teamtripdraw.android.support.framework.presentation.event.EventObserver
import com.teamtripdraw.android.support.framework.presentation.naverMap.initUserInterface
import com.teamtripdraw.android.support.framework.presentation.resolution.toPixel
import com.teamtripdraw.android.ui.common.animation.ObjectAnimators
import com.teamtripdraw.android.ui.common.tripDrawViewModelFactory
import com.teamtripdraw.android.ui.home.markerSelectedBottomSheet.BottomSheetClickSituation
import com.teamtripdraw.android.ui.home.markerSelectedBottomSheet.MarkerSelectedBottomSheet
import com.teamtripdraw.android.ui.post.viewer.PostViewerActivity

class TripDetailActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var naverMap: NaverMap
    private var fabState: Boolean = false

    private lateinit var binding: ActivityTripDetailBinding
    private val viewModel: TripDetailViewModel by viewModels { tripDrawViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_trip_detail)
        binding.lifecycleOwner = this

        binding.tripDetailViewModel = viewModel
        binding.fabState = fabState

        initTripInfo()
        matchMapFragmentToNaverMap()
        initFloatingButtonClickListener()
        setUpPostViewerClickEvent()
        initMarkerSelectedObserver()
        setClickBack()
    }

    private fun initTripInfo() {
        val tripId = intent.getLongExtra(TRIP_ID_KEY, Trip.NULL_SUBSTITUTE_ID)
        viewModel.updateTripId(tripId)
        viewModel.updateTripInfo()
    }

    private fun matchMapFragmentToNaverMap() {
        val fragmentManager = supportFragmentManager
        val mapFragment =
            fragmentManager.findFragmentById(R.id.fragment_trip_detail_map) as MapFragment?
                ?: MapFragment.newInstance().also {
                    fragmentManager.beginTransaction().add(R.id.fragment_trip_detail_map, it)
                        .commit()
                }
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        binding.naverMap = this.naverMap
        this.naverMap.initUserInterface()
        this.naverMap.locationOverlay.icon = currentLocationImage
        this.naverMap.setContentPadding(0, 0, 0, toPixel(this, 67))
    }

    private fun initFloatingButtonClickListener() {
        binding.fabHome.setOnClickListener {
            ObjectAnimators.toggleFab(
                binding.fabHome,
                null,
                binding.fabPostList,
                binding.fabMarkerMode,
                null,
                binding.tvPostList,
                binding.tvMarkerMode,
                fabState,
            )
            fabState = !fabState
            binding.fabState = fabState
        }
    }

    private fun setUpPostViewerClickEvent() {
        viewModel.openPostViewerEvent.observe(
            this,
            EventObserver(this@TripDetailActivity::navigateToPostViewer),
        )
    }

    private fun navigateToPostViewer(isClicked: Boolean) {
        if (isClicked) startActivity(PostViewerActivity.getIntent(this, viewModel.tripId))
    }

    override fun onStop() {
        turnOffFloatingActionButton()
        super.onStop()
    }

    private fun turnOffFloatingActionButton() {
        ObjectAnimators.closeFab(
            binding.fabHome,
            null,
            binding.fabPostList,
            binding.fabMarkerMode,
            null,
            binding.tvPostList,
            binding.tvMarkerMode,
        )
        fabState = false
        binding.fabState = fabState
    }

    private fun initMarkerSelectedObserver() {
        viewModel.makerSelectedEvent.observe(this) { pointId ->
            showMarkerSelectedBottomSheet(pointId)
        }
    }

    private fun showMarkerSelectedBottomSheet(pointId: Long) {
        if (!viewModel.markerSelectedState) {
            val markerSelectedBottomSheet = MarkerSelectedBottomSheet()
            markerSelectedBottomSheet.arguments =
                MarkerSelectedBottomSheet.getBundle(
                    pointId,
                    viewModel.tripId,
                    BottomSheetClickSituation.HISTORY,
                )
            markerSelectedBottomSheet.show(supportFragmentManager, this.javaClass.name)
        }
    }

    private fun setClickBack() {
        binding.onBackClick = { finish() }
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
