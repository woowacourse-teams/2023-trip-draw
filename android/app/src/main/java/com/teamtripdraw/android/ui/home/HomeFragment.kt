package com.teamtripdraw.android.ui.home

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.teamtripdraw.android.databinding.FragmentHomeBinding
import com.teamtripdraw.android.support.framework.presentation.extemsions.checkForeGroundPermission

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(ACCESS_FINE_LOCATION, false) -> {
                moveToCurrentPoint()
            }
            permissions.getOrDefault(ACCESS_COARSE_LOCATION, false) -> {
                moveToCurrentPoint()
                // 세부 항목을 사용 하면 더 정확한 지도 서비스 를 이용할 수 있다는 다이얼로그
            }
            else -> {
                // 권한 받아야 한다는 다이얼로그 띄워주기
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkForeGroundPermission(
            requireContext(),
            locationPermissionRequest,
            this::moveToCurrentPoint
        )
    }

    private fun moveToCurrentPoint() {
        // 네이버 맵 현재 지점 이동 로직 구현
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
