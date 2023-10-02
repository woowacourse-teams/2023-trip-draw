package com.teamtripdraw.android.ui.common.filter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.teamtripdraw.android.R
import com.teamtripdraw.android.databinding.ActivityFilterSelectionBinding
import com.teamtripdraw.android.domain.model.filterOption.OptionAgeRange
import com.teamtripdraw.android.domain.model.filterOption.OptionDayOfWeek
import com.teamtripdraw.android.domain.model.filterOption.OptionGender
import com.teamtripdraw.android.domain.model.filterOption.OptionHour
import com.teamtripdraw.android.domain.model.filterOption.OptionMonth
import com.teamtripdraw.android.domain.model.filterOption.OptionYear

class FilterSelectionActivity : AppCompatActivity() {

    lateinit var binding: ActivityFilterSelectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_filter_selection)

        binding.filterOptionsYear.apply {
            setTitle("Year", FilterOptionsView.TEXT_APPEARANCE_SUBTITLE)
            setupOptions(OptionYear.values().toList())
        }

        binding.filterOptionsMonth.apply {
            setTitle("Month", FilterOptionsView.TEXT_APPEARANCE_SUBTITLE)
            setupOptions(OptionMonth.values().toList())
        }
        binding.filterOptionsDayOfWeek.apply {
            setTitle("Day Of Week", FilterOptionsView.TEXT_APPEARANCE_SUBTITLE)
            setupOptions(OptionDayOfWeek.values().toList())
        }

        binding.filterOptionsHourFrom.apply {
            minValue = OptionHour.values().first().value as Int
            maxValue = OptionHour.values().last().value as Int
        }

        binding.filterOptionsHourTo.apply {
            minValue = OptionHour.values().first().value as Int
            maxValue = OptionHour.values().last().value as Int
            value = maxValue
        }

        binding.filterOptionsAgeRange.apply {
            setTitle("연령대", FilterOptionsView.TEXT_APPEARANCE_TITLE)
            setupOptions(OptionAgeRange.values().toList())
        }

        binding.filterOptionsGender.apply {
            setTitle("성별", FilterOptionsView.TEXT_APPEARANCE_TITLE)
            setupOptions(OptionGender.values().toList())
        }

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
