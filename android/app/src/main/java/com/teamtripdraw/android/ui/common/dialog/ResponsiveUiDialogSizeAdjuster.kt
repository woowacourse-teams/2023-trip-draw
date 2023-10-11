package com.teamtripdraw.android.ui.common.dialog

import android.app.Dialog
import android.content.res.Resources
import android.view.ViewGroup
import com.teamtripdraw.android.R
import kotlin.math.roundToInt

class ResponsiveUiDialogSizeAdjuster {

    fun adjustSize(dialog: Dialog?, resources: Resources) {
        dialog ?: return
        when (isOverPhoneSize(resources)) {
            true -> adjustTabletDialogSize(dialog, resources)
            false -> adjustPhoneDialogSize(dialog, resources)
        }
    }

    private fun isOverPhoneSize(resources: Resources): Boolean =
        getDeviceDPValue(resources) >= DEVICE_TYPE_BOUNDARY

    private fun getDeviceDPValue(resources: Resources): Int =
        (resources.displayMetrics.widthPixels / resources.displayMetrics.density).roundToInt()

    private fun adjustPhoneDialogSize(dialog: Dialog, resources: Resources) {
        dialog.window?.apply {
            setLayout(
                (resources.displayMetrics.widthPixels * PHONE_DIALOG_WIDTH_RATIO).toInt(),
                ViewGroup.LayoutParams.WRAP_CONTENT,
            )
            setBackgroundDrawableResource(R.color.td_white)
        }
    }

    private fun adjustTabletDialogSize(dialog: Dialog, resources: Resources) {
        dialog.window?.apply {
            setLayout(
                dPToPixel(TABLET_DIALOG_WIDTH_DP, resources),
                ViewGroup.LayoutParams.WRAP_CONTENT,
            )
            setBackgroundDrawableResource(R.color.td_white)
        }
    }

    private fun dPToPixel(dp: Int, resources: Resources): Int =
        (dp * resources.displayMetrics.density).roundToInt()

    companion object {
        private const val PHONE_DIALOG_WIDTH_RATIO = 0.85
        private const val DEVICE_TYPE_BOUNDARY = 600
        private const val TABLET_DIALOG_WIDTH_DP = 400
    }
}
