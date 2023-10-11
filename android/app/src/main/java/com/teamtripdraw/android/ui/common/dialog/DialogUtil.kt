package com.teamtripdraw.android.ui.common.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.teamtripdraw.android.R
import com.teamtripdraw.android.databinding.DialogUtilBinding

class DialogUtil(
    private val dialogMode: Int,
    private val doAfterConfirm: () -> Unit,
) : DialogFragment() {

    private var _binding: DialogUtilBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.dialog_util, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        isCancelable = true
        setMessage()
        setConfirmText()
        setConfirmTextColor()
        setConfirmTextClickListener()
        setCancelTextClickListener()
    }

    override fun onStart() {
        super.onStart()
        ResponsiveUiDialogSizeAdjuster().adjustSize(dialog, resources)
    }

    private fun setMessage() {
        binding.tvDialogUtilContent.text = when (dialogMode) {
            DELETE_CHECK -> getString(R.string.tv_content_delete_check)
            SAVE_CHECK -> getString(R.string.tv_content_save_check)
            LOGOUT_CHECK -> getString(R.string.tv_content_logout_check)
            UNSUBSCRIBE_CHECK -> getString(R.string.tv_content_unsubscribe_check)
            else -> throw IllegalStateException()
        }
    }

    private fun setConfirmText() {
        binding.confirmText = when (dialogMode) {
            DELETE_CHECK -> getString(R.string.tv_confirm_delete_check)
            SAVE_CHECK -> getString(R.string.tv_confirm_save_check)
            LOGOUT_CHECK -> getString(R.string.tv_confirm_logout_check)
            UNSUBSCRIBE_CHECK -> getString(R.string.tv_confirm_unsubscribe_check)
            else -> throw IllegalStateException()
        }
    }

    private fun setConfirmTextColor() {
        val textColor = when (dialogMode) {
            DELETE_CHECK -> getColor(requireContext(), R.color.td_red)
            SAVE_CHECK -> getColor(requireContext(), R.color.td_sub_blue)
            LOGOUT_CHECK -> getColor(requireContext(), R.color.td_red)
            UNSUBSCRIBE_CHECK -> getColor(requireContext(), R.color.td_red)
            else -> throw IllegalStateException()
        }
        binding.tvDialogUtilConfirm.setTextColor(textColor)
    }

    private fun setConfirmTextClickListener() {
        binding.tvDialogUtilConfirm.setOnClickListener {
            doAfterConfirm()
            dismiss()
        }
    }

    private fun setCancelTextClickListener() {
        binding.tvDialogUtilCancel.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val DELETE_CHECK = 0
        const val SAVE_CHECK = 1
        const val LOGOUT_CHECK = 2
        const val UNSUBSCRIBE_CHECK = 3
    }
}
