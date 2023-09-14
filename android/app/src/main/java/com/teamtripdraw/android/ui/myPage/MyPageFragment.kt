package com.teamtripdraw.android.ui.myPage

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.teamtripdraw.android.databinding.FragmentMyPageBinding
import com.teamtripdraw.android.domain.model.trip.Trip
import com.teamtripdraw.android.support.framework.presentation.event.EventObserver
import com.teamtripdraw.android.ui.common.dialog.DialogUtil
import com.teamtripdraw.android.ui.common.tripDrawViewModelFactory
import com.teamtripdraw.android.ui.home.recordingPoint.RecordingPointAlarmManager
import com.teamtripdraw.android.ui.home.recordingPoint.RecordingPointService
import com.teamtripdraw.android.ui.login.LoginActivity
import com.teamtripdraw.android.ui.policy.OpenSourceLicenseActivity
import com.teamtripdraw.android.ui.policy.PrivacyPolicyActivity

class MyPageFragment : Fragment() {

    private var _binding: FragmentMyPageBinding? = null
    private val binding: FragmentMyPageBinding get() = _binding!!
    private val viewModel: MyPageViewModel by viewModels { tripDrawViewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMyPageBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.myPageViewModel = viewModel

        initNickname()
        initObserver()

        return binding.root
    }

    private fun initObserver() {
        initOpenOpenSourceLicenseObserver()
        initOpenPrivacyPolicyObserver()
        initLogOutEventObserver()
        initUnsubscribeEventObserver()
        initUnsubscribeSuccessEventObserver()
    }

    private fun initNickname() {
        viewModel.fetchNickname()
    }

    private fun initOpenOpenSourceLicenseObserver() {
        viewModel.openOpenSourceLicenseEvent.observe(viewLifecycleOwner) {
            if (it) startActivity(OpenSourceLicenseActivity.getIntent(requireContext()))
        }
    }

    private fun initOpenPrivacyPolicyObserver() {
        viewModel.openPrivacyPolicyEvent.observe(viewLifecycleOwner) {
            if (it) startActivity(PrivacyPolicyActivity.getIntent(requireContext()))
        }
    }

    private fun initLogOutEventObserver() {
        viewModel.logoutEvent.observe(viewLifecycleOwner, this::logoutEventListener)
    }

    private fun logoutEventListener(event: Boolean) {
        if (event) {
            DialogUtil(DialogUtil.LOGOUT_CHECK) {
                viewModel.logout()
                finishTravelIfInProgress()
                navigateToLoginActivity()
            }.show(childFragmentManager, this.javaClass.name)
            viewModel.resetLogoutEvent()
        }
    }

    private fun initUnsubscribeEventObserver() {
        viewModel.unsubscribeEvent.observe(viewLifecycleOwner, this::unsubscribeEventListener)
    }

    private fun unsubscribeEventListener(event: Boolean) {
        if (event) {
            DialogUtil(DialogUtil.UNSUBSCRIBE_CHECK) {
                viewModel.unsubscribe()
            }.show(childFragmentManager, this.javaClass.name)
            viewModel.resetUnsubscribeEvent()
        }
    }

    private fun initUnsubscribeSuccessEventObserver() {
        viewModel.unsubscribeSuccessEvent.observe(
            viewLifecycleOwner,
            EventObserver(this::unsubscribeSuccessEventListener),
        )
    }

    private fun unsubscribeSuccessEventListener(event: Boolean) {
        if (event) {
            finishTravelIfInProgress()
            navigateToLoginActivity()
        }
    }

    private fun finishTravelIfInProgress() {
        if (viewModel.currentTripId != Trip.NULL_SUBSTITUTE_ID) {
            stopRecordingPointAlarmManager()
            stopRecordingPointService()
            clearCurrentTripId()
        }
    }

    private fun stopRecordingPointAlarmManager() {
        RecordingPointAlarmManager(requireContext()).cancelRecord()
    }

    private fun stopRecordingPointService() {
        val recordingPointServiceIntent =
            Intent(requireContext(), RecordingPointService::class.java)
        requireActivity().stopService(recordingPointServiceIntent)
    }

    private fun clearCurrentTripId() {
        viewModel.clearCurrentTripId()
    }

    private fun navigateToLoginActivity() {
        val intent = LoginActivity.getIntent(requireContext())
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
