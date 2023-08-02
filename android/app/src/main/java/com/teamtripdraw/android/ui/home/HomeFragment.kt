package com.teamtripdraw.android.ui.home

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.util.FusedLocationSource
import com.teamtripdraw.android.R
import com.teamtripdraw.android.databinding.FragmentHomeBinding
import com.teamtripdraw.android.support.framework.presentation.event.EventObserver
import com.teamtripdraw.android.support.framework.presentation.naverMap.LOCATION_PERMISSION_REQUEST_CODE
import com.teamtripdraw.android.support.framework.presentation.naverMap.initUserInterface
import com.teamtripdraw.android.support.framework.presentation.naverMap.initUserLocationOption
import com.teamtripdraw.android.support.framework.presentation.permission.checkForeGroundPermission
import com.teamtripdraw.android.support.framework.presentation.permission.checkNotificationPermission
import com.teamtripdraw.android.support.framework.presentation.resolution.toPixel
import com.teamtripdraw.android.ui.common.tripDrawViewModelFactory
import com.teamtripdraw.android.ui.home.recordingPoint.RecordingPointAlarmManager
import com.teamtripdraw.android.ui.home.recordingPoint.RecordingPointService

class HomeFragment : Fragment(), OnMapReadyCallback {

    private lateinit var naverMap: NaverMap

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by viewModels { tripDrawViewModelFactory }

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
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
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            // todo: 알림 권한 받아야 notification 보여줄 수 있다는 다이얼로그 #75
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.homeViewModel = homeViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkForeGroundPermission(requireContext(), locationPermissionRequest)
        matchMapFragmentToNaverMap()
        observeStartTripEvent()
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
        this.naverMap.initUserInterface()
        this.naverMap.initUserLocationOption(
            FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE),
            requireContext()
        )
        this.naverMap.setContentPadding(0, 0, 0, toPixel(requireContext(), 67))
    }

    private fun observeStartTripEvent() {
        homeViewModel.startTripEvent.observe(
            viewLifecycleOwner,
            EventObserver(this::startRecordPoint)
        )
    }

    private fun startRecordPoint(isStartedTrip: Boolean) {
        if (isStartedTrip) {
            startRecordingPointService()
            startRecordingPointAlarmManager()
        }
    }

    private fun startRecordingPointAlarmManager() {
        RecordingPointAlarmManager(requireContext()).startRecord(homeViewModel.tripId)
    }

    private fun startRecordingPointService() {
        RecordingPointService.getTripIdIntent(
            Intent(requireContext(), RecordingPointService::class.java),
            homeViewModel.tripId
        ).apply { requireContext().startService(this) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
