package com.teamtripdraw.android.ui.common.filter

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.teamtripdraw.android.R
import com.teamtripdraw.android.databinding.ActivityFilterSelectionBinding
import com.teamtripdraw.android.domain.model.filterOption.OptionHour

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
    }
}
