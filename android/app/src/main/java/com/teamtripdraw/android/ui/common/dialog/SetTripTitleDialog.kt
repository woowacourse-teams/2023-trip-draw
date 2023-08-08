package com.teamtripdraw.android.ui.common.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.teamtripdraw.android.R
import com.teamtripdraw.android.databinding.FragmentTripTitleDialogBinding
import com.teamtripdraw.android.support.framework.presentation.event.EventObserver
import com.teamtripdraw.android.ui.common.tripDrawViewModelFactory

class SetTripTitleDialog(
    private val tripId: Long,
    private val status: SetTitleStatus
) : DialogFragment() {

    private var _binding: FragmentTripTitleDialogBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SetTripTitleDialogViewModel by viewModels { tripDrawViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTripTitleDialogBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.updateTripId(tripId)
        binding.insertTripTitleDialogViewModel = viewModel
        initObserve()
    }

    override fun onStart() {
        super.onStart()
        setLayout()
    }

    private fun setLayout() {
        requireNotNull(dialog).apply {
            requireNotNull(window).apply {
                setLayout(
                    (resources.displayMetrics.widthPixels * 0.85).toInt(),
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                setBackgroundDrawableResource(R.color.td_white)
            }
        }
    }

    private fun initObserve() {
        viewModel.isBlankEvent.observe(
            viewLifecycleOwner,
            EventObserver(this@SetTripTitleDialog::onBlankTitle)
        )

        viewModel.titleSetupCompletedEvent.observe(
            viewLifecycleOwner,
            EventObserver(this@SetTripTitleDialog::onSetupCompleted)
        )
    }

    private fun onBlankTitle(isBlank: Boolean) {
        if (isBlank) {
            Log.e("BLANK_TITLE_ERROR", BLANK_TITLE_ERROR)
            Snackbar.make(
                binding.root,
                BLANK_TITLE_ERROR,
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    private fun onSetupCompleted(isSuccess: Boolean) {
        if (isSuccess) {
            when (status) {
                SetTitleStatus.FINISHED -> navigateDetailPage(requireNotNull(viewModel.tripId.value))
                SetTitleStatus.EDIT -> dismiss()
            }
        }
    }

    private fun navigateDetailPage(tripId: Long) {
        // todo 해당 여행 히스토리의 상세 화면으로 이동
        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val BLANK_TITLE_ERROR = "제목을 입력해주세요"
    }
}
