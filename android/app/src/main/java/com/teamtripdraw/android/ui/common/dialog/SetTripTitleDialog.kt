package com.teamtripdraw.android.ui.common.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.teamtripdraw.android.R
import com.teamtripdraw.android.databinding.FragmentTripTitleDialogBinding
import com.teamtripdraw.android.support.framework.presentation.event.EventObserver
import com.teamtripdraw.android.support.framework.presentation.getParcelableCompat
import com.teamtripdraw.android.ui.history.detail.HistoryDetailActivity
import com.teamtripdraw.android.ui.history.detail.HistoryDetailViewModel
import com.teamtripdraw.android.ui.home.HomeViewModel
import com.teamtripdraw.android.ui.model.UiPreviewTrip

class SetTripTitleDialog : DialogFragment() {

    private var _binding: FragmentTripTitleDialogBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SetTripTitleDialogViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels({ requireParentFragment() })
    private val historyDetailViewModel: HistoryDetailViewModel by activityViewModels()

    private val tripId by lazy { requireArguments().getLong(TRIP_ID_KEY) }
    private val status by lazy {
        requireArguments().getParcelableCompat<SetTitleSituation>(
            SET_TITLE_SITUATION_KEY,
        ) ?: throw IllegalStateException()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentTripTitleDialogBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        isCancelable = true
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.updateTripId(tripId)
        binding.insertTripTitleDialogViewModel = viewModel
        initCompletedEventObserve()
        initPreviewTripObserve()
    }

    override fun onStart() {
        super.onStart()
        setLayout()
    }

    private fun setLayout() {
        requireNotNull(dialog).apply {
            requireNotNull(window).apply {
                setLayout(
                    (resources.displayMetrics.widthPixels * DIALOG_WINDOW_SIZE).toInt(),
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                )
                setBackgroundDrawableResource(R.color.td_white)
            }
        }
    }

    private fun initCompletedEventObserve() {
        viewModel.titleSetupCompletedEvent.observe(
            viewLifecycleOwner,
            EventObserver(this@SetTripTitleDialog::onSetupCompleted),
        )
    }

    private fun initPreviewTripObserve() {
        viewModel.previewTrip.observe(viewLifecycleOwner) {
            navigateToHistoryDetail(it)
        }
    }

    private fun onSetupCompleted(isSuccess: Boolean) {
        if (isSuccess) {
            when (status) {
                SetTitleSituation.FINISHED -> {
                    viewModel.getTripPreviewInfo()
                    homeViewModel.finishTripCompleteEvent()
                }
                SetTitleSituation.EDIT -> {
                    historyDetailViewModel.updateTripTitle(viewModel.tripTitle.value ?: "")
                }
            }
            dismiss()
        }
    }

    private fun navigateToHistoryDetail(trip: UiPreviewTrip) =
        startActivity(HistoryDetailActivity.getIntent(requireContext(), trip))

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val DIALOG_WINDOW_SIZE = 0.85
        private const val TRIP_ID_KEY = "TRIP_ID_KEY"
        private const val SET_TITLE_SITUATION_KEY = "SET_TITLE_SITUATION_KEY"

        fun getBundle(tripId: Long, setTitleSituation: SetTitleSituation): Bundle =
            Bundle().apply {
                putLong(TRIP_ID_KEY, tripId)
                putSerializable(SET_TITLE_SITUATION_KEY, setTitleSituation)
            }
    }
}
