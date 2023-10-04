package com.teamtripdraw.android.ui.filter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.teamtripdraw.android.R
import com.teamtripdraw.android.databinding.ActivityFilterSelectionBinding
import com.teamtripdraw.android.domain.model.filterOption.OptionHour
import com.teamtripdraw.android.support.framework.presentation.getParcelableExtraCompat

class FilterSelectionActivity : AppCompatActivity() {

    lateinit var binding: ActivityFilterSelectionBinding
    private val viewModel: FilterSelectionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_filter_selection)
        binding.lifecycleOwner = this
        binding.filterSelectionViewModel = viewModel

        binding.btnFilterSearch.setOnClickListener {
            val years = binding.filterOptionsYear.getCheckedChipIds()
            val months = binding.filterOptionsMonth.getCheckedChipIds()
            val dayOfWeeks = binding.filterOptionsDayOfWeek.getCheckedChipIds()

            val hour = OptionHour.getSelectedHourIds(
                fromValue = binding.filterOptionsHourFrom.value,
                toValue = binding.filterOptionsHourTo.value,
            )
            val ageRange = binding.filterOptionsAgeRange.getCheckedChipIds()
            val gender = binding.filterOptionsGender.getCheckedChipIds()
        }
        viewModel.setupFilterType(initFilterType())
    }

    private fun initFilterType(): FilterType =
        intent.getParcelableExtraCompat(FILTER_TYPE_ID) ?: throw IllegalStateException()

    companion object {
        private const val FILTER_TYPE_ID = "FILTER_TYPE_ID"

        fun getIntent(context: Context, type: FilterType): Intent {
            val intent = Intent(context, FilterSelectionActivity::class.java)
            intent.putExtra(FILTER_TYPE_ID, type as Parcelable)
            return intent
        }
    }
}
