package com.teamtripdraw.android.ui.myPage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.teamtripdraw.android.databinding.FragmentMyPageBinding
import com.teamtripdraw.android.support.framework.presentation.event.EventObserver
import com.teamtripdraw.android.ui.common.tripDrawViewModelFactory
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
        initSignOutObserve()
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

    private fun initSignOutObserve() {
        viewModel.logoutEvent.observe(
            viewLifecycleOwner,
            EventObserver {
                // todo : 화면을 종료시키고, 로그인 화면으로 전환
            },
        )
    }

    private fun initAccountDeletionObserve() {
        viewModel.openAccountDeletionEvent.observe(viewLifecycleOwner) {
            if (it) startActivity(AccountDeletionActivity.getIntent(requireContext()))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
