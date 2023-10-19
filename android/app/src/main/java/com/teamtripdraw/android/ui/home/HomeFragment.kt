package com.teamtripdraw.android.ui.home

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import com.teamtripdraw.android.R
import com.teamtripdraw.android.databinding.FragmentHomeBinding
import com.teamtripdraw.android.domain.model.point.Point
import com.teamtripdraw.android.domain.model.trip.Trip
import com.teamtripdraw.android.support.framework.presentation.Locations.getUpdateLocation
import com.teamtripdraw.android.support.framework.presentation.event.EventObserver
import com.teamtripdraw.android.support.framework.presentation.naverMap.LOCATION_PERMISSION_REQUEST_CODE
import com.teamtripdraw.android.support.framework.presentation.naverMap.initUserInterface
import com.teamtripdraw.android.support.framework.presentation.naverMap.initUserLocationOption
import com.teamtripdraw.android.support.framework.presentation.permission.checkForeGroundPermission
import com.teamtripdraw.android.support.framework.presentation.permission.checkNotificationPermission
import com.teamtripdraw.android.support.framework.presentation.resolution.toPixel
import com.teamtripdraw.android.ui.common.animation.ObjectAnimators
import com.teamtripdraw.android.ui.common.dialog.SetTitleSituation.FINISHED
import com.teamtripdraw.android.ui.common.dialog.SetTripTitleDialog
import com.teamtripdraw.android.ui.home.markerSelectedBottomSheet.BottomSheetClickSituation
import com.teamtripdraw.android.ui.home.markerSelectedBottomSheet.MarkerSelectedBottomSheet
import com.teamtripdraw.android.ui.home.recordingPoint.RecordingPointAlarmManager
import com.teamtripdraw.android.ui.home.recordingPoint.RecordingPointService
import com.teamtripdraw.android.ui.post.viewer.PostViewerActivity
import com.teamtripdraw.android.ui.post.writing.PostWritingActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(), OnMapReadyCallback {

    private lateinit var naverMap: NaverMap
    private var fabState: Boolean = false

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by viewModels()

    private var recordingPointServiceBindingState: Boolean = false

    private lateinit var recordingPointBinder: RecordingPointService.RecordingPointBinder

    private lateinit var updatedTripId: LiveData<Long>

    private val recordingPointServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            recordingPointBinder = service as RecordingPointService.RecordingPointBinder
            recordingPointBinder.updatedTripIdHolderInitializeState()
            updatedTripId = recordingPointBinder.getUpdatedTripIdHolder()
            recordingPointServiceBindingState = true
            observeUpdateTripId()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            recordingPointBinder.updatedTripIdHolderInitializeState()
            recordingPointServiceBindingState = false
        }
    }

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions(),
    ) { permissions ->
        when {
            permissions.getOrDefault(ACCESS_COARSE_LOCATION, false) -> {
                // todo: 세부 항목을 사용 하면 더 정확한 지도 서비스 를 이용할 수 있다는 다이얼로그 #15
            }
            else -> {
                // todo: 권한 받아야 한다는 다이얼로그 띄워주기 #15
            }
        }
        // todo 권한 관련 분리 필수 !!!!!!!!!!! #134 참고
        checkNotificationPermission(requireContext(), notificationPermissionRequest)
    }
    private val notificationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted ->
        if (!isGranted) {
            // todo: 알림 권한 받아야 notification 보여줄 수 있다는 다이얼로그 #75
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.homeViewModel = homeViewModel
        binding.fabState = fabState
        if (homeViewModel.tripId != Trip.NULL_SUBSTITUTE_ID) bindRecordingPointService()
        return binding.root
    }

    private fun bindRecordingPointService() {
        Intent(requireContext(), RecordingPointService::class.java).also { intent ->
            requireActivity().bindService(
                intent,
                recordingPointServiceConnection,
                Context.BIND_AUTO_CREATE,
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkForeGroundPermission(requireContext(), locationPermissionRequest)
        matchMapFragmentToNaverMap()
        observeStartTripEvent()
        initFloatingButtonClickListener()
        setUpPostViewerClickEvent()
        setUpPostWritingClickEvent()
        initPostWritingEventObserver()
        initMarkerSelectedObserver()
        initFinishTripEventObserver()
        initFinishTripCompleteEventObserver()
    }

    private fun matchMapFragmentToNaverMap() {
        val fragmentManager = childFragmentManager
        val mapFragment = fragmentManager.findFragmentById(R.id.fragment_map) as MapFragment?
            ?: MapFragment.newInstance().also {
                fragmentManager.beginTransaction().add(R.id.fragment_map, it).commit()
            }
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        binding.naverMap = this.naverMap
        this.naverMap.initUserInterface()
        this.naverMap.initUserLocationOption(
            FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE),
            requireContext(),
        )
        this.naverMap.locationOverlay.icon = currentLocationImage
        this.naverMap.setContentPadding(0, 0, 0, toPixel(requireContext(), 67))
    }

    private fun observeStartTripEvent() {
        homeViewModel.startTripEvent.observe(
            viewLifecycleOwner,
            EventObserver(this::startRecordPoint),
        )
    }

    private fun startRecordPoint(isStartedTrip: Boolean) {
        if (isStartedTrip) {
            startRecordingPointService()
            startRecordingPointAlarmManager()
            bindRecordingPointService()
        }
    }

    private fun startRecordingPointAlarmManager() {
        RecordingPointAlarmManager(requireContext()).startRecord(homeViewModel.tripId)
    }

    private fun startRecordingPointService() {
        RecordingPointService.getTripIdIntent(
            Intent(requireContext(), RecordingPointService::class.java),
            homeViewModel.tripId,
        ).apply { requireContext().startService(this) }
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
                fabState,
            )
            fabState = !fabState
            binding.fabState = fabState
        }
    }

    private fun setUpPostViewerClickEvent() {
        homeViewModel.openPostViewerEvent.observe(
            viewLifecycleOwner,
            EventObserver(this@HomeFragment::navigateToPostViewer),
        )
    }

    private fun navigateToPostViewer(isClicked: Boolean) {
        if (isClicked) {
            startActivity(
                PostViewerActivity.getIntent(
                    requireContext(),
                    homeViewModel.tripId,
                ),
            )
        }
    }

    private fun setUpPostWritingClickEvent() {
        binding.fabWritePost.setOnClickListener {
            getUpdateLocation(
                getFusedLocationClient(),
                requireContext(),
                homeViewModel::openPostWriting,
            )
        }
    }

    private fun getFusedLocationClient(): FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(requireContext())

    private fun initPostWritingEventObserver() {
        homeViewModel.openPostWritingEvent.observe(
            viewLifecycleOwner,
            EventObserver(this::navigateToPostWriting),
        )
    }

    private fun navigateToPostWriting(locationResult: LocationResult) {
        val tripId = homeViewModel.tripId
        startActivity(
            PostWritingActivity.getIntentByLatLng(
                context = requireContext(),
                tripId = tripId,
                latitude = locationResult.locations.first().latitude,
                longitude = locationResult.locations.first().longitude,
            ),
        )
    }

    private fun observeUpdateTripId() {
        updatedTripId.observe(viewLifecycleOwner) {
            // map 좌표 최신화 방법 #193 참고
            if (it != Point.NULL_SUBSTITUTE_ID) homeViewModel.updateTripInfo()
        }
    }

    override fun onStop() {
        turnOffFloatingActionButton()
        super.onStop()
    }

    private fun turnOffFloatingActionButton() {
        ObjectAnimators.closeFab(
            binding.fabHome,
            binding.fabWritePost,
            binding.fabPostList,
            binding.fabMarkerMode,
            binding.tvWritePost,
            binding.tvPostList,
            binding.tvMarkerMode,
        )
        fabState = false
        binding.fabState = fabState
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (homeViewModel.tripId != Trip.NULL_SUBSTITUTE_ID && recordingPointServiceBindingState) {
            unbindRecordingPointService()
        }
        _binding = null
    }

    private fun unbindRecordingPointService() {
        requireActivity().unbindService(recordingPointServiceConnection)
        recordingPointServiceBindingState = false
    }

    private fun initMarkerSelectedObserver() {
        homeViewModel.makerSelectedEvent.observe(viewLifecycleOwner) { pointId ->
            showMarkerSelectedBottomSheet(pointId)
        }
    }

    private fun showMarkerSelectedBottomSheet(pointId: Long) {
        if (!homeViewModel.markerSelectedState) {
            val markerSelectedBottomSheet = MarkerSelectedBottomSheet()
            markerSelectedBottomSheet.arguments =
                MarkerSelectedBottomSheet.getBundle(
                    pointId,
                    homeViewModel.tripId,
                    BottomSheetClickSituation.HOME,
                )
            markerSelectedBottomSheet.show(childFragmentManager, this.javaClass.name)
        }
    }

    private fun initFinishTripEventObserver() {
        homeViewModel.finishTripEvent.observe(viewLifecycleOwner, this::showSetTripTitleDialog)
    }

    private fun showSetTripTitleDialog(event: Boolean) {
        if (event) {
            val setTripTitleDialog = SetTripTitleDialog()
            setTripTitleDialog.arguments =
                SetTripTitleDialog.getBundle(homeViewModel.tripId, FINISHED)
            setTripTitleDialog.show(childFragmentManager, this.javaClass.name)
            homeViewModel.resetFinishTripEvent()
        }
    }

    private fun initFinishTripCompleteEventObserver() {
        homeViewModel.finishTripCompleteEvent.observe(
            viewLifecycleOwner,
            this::finishTripSuccessListener,
        )
    }

    private fun finishTripSuccessListener(event: Boolean) {
        if (event) {
            updateFinishTripHomeUiState()
            stopRecordingPointAlarmManager()
            stopRecordingPointService()
            unbindRecordingPointService()
            clearCurrentTripId()
            clearCurrentTripRoute()
            homeViewModel.resetFinishTripCompleteEvent()
        }
    }

    private fun updateFinishTripHomeUiState() {
        homeViewModel.updateHomeUiState(HomeUiState.BEFORE_TRIP)
    }

    private fun clearCurrentTripId() {
        homeViewModel.clearCurrentTripId()
    }

    private fun stopRecordingPointService() {
        val recordingPointServiceIntent =
            Intent(requireContext(), RecordingPointService::class.java)
        requireActivity().stopService(recordingPointServiceIntent)
    }

    private fun stopRecordingPointAlarmManager() {
        RecordingPointAlarmManager(requireContext()).cancelRecord()
    }

    private fun clearCurrentTripRoute() {
        homeViewModel.cleatCurrentTripRoute()
    }

    companion object {
        private val currentLocationImage = OverlayImage.fromResource(R.drawable.ic_current_location)
    }
}
