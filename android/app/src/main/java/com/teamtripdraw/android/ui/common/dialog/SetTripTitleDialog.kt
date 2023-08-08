package com.teamtripdraw.android.ui.common.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.teamtripdraw.android.R
import com.teamtripdraw.android.databinding.FragmentTripTitleDialogBinding
import com.teamtripdraw.android.support.framework.presentation.event.EventObserver
import com.teamtripdraw.android.ui.common.tripDrawViewModelFactory
import com.teamtripdraw.android.ui.model.UiTripItem

class SetTripTitleDialog(
    private val tripId: Long,
    private val status: SetTitleSituation
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
        viewModel.titleSetupCompletedEvent.observe(
            viewLifecycleOwner,
            EventObserver(this@SetTripTitleDialog::onSetupCompleted)
        )
    }

    private fun onSetupCompleted(isSuccess: Boolean) {
        if (isSuccess) {
            when (status) {
                SetTitleSituation.FINISHED -> navigateDetailPage(requireNotNull(viewModel.tripItem.value))
                SetTitleSituation.EDIT -> dismiss()
            }
        }
    }

    private fun navigateDetailPage(tripItem: UiTripItem) {
        // todo 해당 여행 히스토리의 상세 화면으로 이동

        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
