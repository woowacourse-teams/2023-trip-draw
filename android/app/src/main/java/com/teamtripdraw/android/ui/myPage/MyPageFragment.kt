package com.teamtripdraw.android.ui.myPage

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.teamtripdraw.android.databinding.FragmentMyPageBinding
import com.teamtripdraw.android.domain.constants.NULL_SUBSTITUTE_TRIP_ID
import com.teamtripdraw.android.ui.common.dialog.DialogUtil
import com.teamtripdraw.android.ui.common.tripDrawViewModelFactory
import com.teamtripdraw.android.ui.home.recordingPoint.RecordingPointAlarmManager
import com.teamtripdraw.android.ui.home.recordingPoint.RecordingPointService
import com.teamtripdraw.android.ui.login.LoginActivity
import com.teamtripdraw.android.ui.myPage.accountDeletion.AccountDeletionActivity
import com.teamtripdraw.android.ui.policy.PrivacyPolicy

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
        initOpenPrivacyPolicyObserve()
        initLogOutEventObserve()
        initAccountDeletionObserve()

        return binding.root
    }

    private fun initNickname() {
        viewModel.fetchNickname()
    }

    private fun initOpenPrivacyPolicyObserve() {
        viewModel.openPrivacyPolicyEvent.observe(viewLifecycleOwner) {
            if (it) startActivity(PrivacyPolicy.getIntent(requireContext()))
        }
    }

    private fun initLogOutEventObserve() {
        viewModel.logoutEvent.observe(viewLifecycleOwner, this::logoutEventListener)
    }

    private fun logoutEventListener(event: Boolean) {
        if (event) {
            DialogUtil(DialogUtil.LOGOUT_CHECK) {
                viewModel.logout()
                finishTravelIfInProgress()
                navigateToMainActivity()
            }.show(childFragmentManager, this.javaClass.name)
        }
    }

    private fun initAccountDeletionObserve() {
        viewModel.openAccountDeletionEvent.observe(viewLifecycleOwner) {
            if (it) startActivity(AccountDeletionActivity.getIntent(requireContext()))
        }
    }

    private fun navigateToMainActivity() {
        val intent = LoginActivity.getIntent(requireContext())
        startActivity(intent)
        requireActivity().finish()
    }

    private fun finishTravelIfInProgress() {
        if (viewModel.currentTripId != NULL_SUBSTITUTE_TRIP_ID) {
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
