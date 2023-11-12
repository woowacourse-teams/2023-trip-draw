package com.teamtripdraw.android.ui.myPage

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.teamtripdraw.android.BuildConfig
import com.teamtripdraw.android.databinding.FragmentMyPageBinding
import com.teamtripdraw.android.domain.model.trip.Trip
import com.teamtripdraw.android.support.framework.presentation.event.EventObserver
import com.teamtripdraw.android.ui.common.dialog.DialogUtil
import com.teamtripdraw.android.ui.history.HistoryActivity
import com.teamtripdraw.android.ui.home.recordingPoint.RecordingPointAlarmManager
import com.teamtripdraw.android.ui.home.recordingPoint.RecordingPointService
import com.teamtripdraw.android.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageFragment : Fragment() {

    private var _binding: FragmentMyPageBinding? = null
    private val binding: FragmentMyPageBinding get() = _binding!!
    private val viewModel: MyPageViewModel by viewModels()

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

    private fun initNickname() {
        viewModel.fetchNickname()
    }

    private fun initObserver() {
        initOpenTripHistoryObserver()
        initOpenTermsOfServiceObserver()
        initOpenOpenSourceLicenseObserver()
        initOpenPrivacyPolicyObserver()
        initLogOutEventObserver()
        initUnsubscribeEventObserver()
        initUnsubscribeSuccessEventObserver()
    }

    private fun initOpenTripHistoryObserver() {
        viewModel.openTripHistoryEvent.observe(viewLifecycleOwner) {
            if (it) startActivity(HistoryActivity.getIntent(requireContext()))
        }
    }

    private fun initOpenTermsOfServiceObserver() {
        viewModel.openTermsOfService.observe(viewLifecycleOwner) {
            if (it.not()) return@observe
            val browserIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse(BuildConfig.TERMS_OF_SERVICE_URL))
            startActivity(browserIntent)
        }
    }

    private fun initOpenOpenSourceLicenseObserver() {
        viewModel.openOpenSourceLicenseEvent.observe(viewLifecycleOwner) {
            if (it.not()) return@observe
            val browserIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse(BuildConfig.OPEN_SOURCE_LICENSE_URL))
            startActivity(browserIntent)
        }
    }

    private fun initOpenPrivacyPolicyObserver() {
        viewModel.openPrivacyPolicyEvent.observe(viewLifecycleOwner) {
            if (it.not()) return@observe
            val browserIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse(BuildConfig.PRIVACY_POLICY_URL))
            startActivity(browserIntent)
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
        val intent = LoginActivity.getIntent(
            requireContext(),
            Intent.FLAG_ACTIVITY_CLEAR_TASK,
            Intent.FLAG_ACTIVITY_NEW_TASK,
        )
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
