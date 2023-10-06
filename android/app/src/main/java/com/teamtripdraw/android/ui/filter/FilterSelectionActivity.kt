package com.teamtripdraw.android.ui.filter

import android.app.Activity
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

        viewModel.setupFilterType(initFilterType())
        initObserver()

        binding.btnRefreshFilter.setOnClickListener {
            binding.apply {
                filterOptionsYear.clearCheck()
                filterOptionsMonth.clearCheck()
                filterOptionsDayOfWeek.clearCheck()
                filterOptionsHourFrom.value = filterOptionsHourFrom.minValue
                filterOptionsHourTo.value = filterOptionsHourFrom.maxValue
                filterOptionsAgeRange.clearCheck()
                filterOptionsGender.clearCheck()
            }
        }
    }

    private fun initFilterType(): FilterType =
        intent.getParcelableExtraCompat(FILTER_TYPE_ID) ?: throw IllegalStateException()

    private fun initObserver() {
        initOpenSearchResultEventObserve()
    }

    private fun initOpenSearchResultEventObserve() {
        viewModel.openSearchResultEvent.observe(
            this,
            this::onPostClick,
        )
    }

    private fun onPostClick(isClicked: Boolean) {
        if (isClicked) {
            val resultIntent = Intent()
            resultIntent.putExtra(SELECTED_OPTIONS_INTENT_KEY, getSelectedOptions())
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }

    private fun getSelectedOptions(): SelectedOptions {
        val selectedOptions = SelectedOptionsBuilder()
        binding.apply {
            selectedOptions.setYears(filterOptionsYear.getCheckedChipIds())
                .setMonths(filterOptionsMonth.getCheckedChipIds())
                .setDaysOfWeek(filterOptionsDayOfWeek.getCheckedChipIds())
                .setAgeRanges(filterOptionsAgeRange.getCheckedChipIds())
                .setGenders(filterOptionsGender.getCheckedChipIds())
                .setAddress("임시 주소입니다")
                .setHours(getHour())
        }
        return selectedOptions.build()
    }

    private fun getHour(): List<Int>? =
        if (viewModel.filterType.value == FilterType.POST) {
            OptionHour.getSelectedHourIds(
                fromValue = binding.filterOptionsHourFrom.value,
                toValue = binding.filterOptionsHourTo.value,
            )
        } else {
            null
        }

    companion object {
        private const val FILTER_TYPE_ID = "FILTER_TYPE_ID"
        const val SELECTED_OPTIONS_INTENT_KEY = "SELECTED_OPTIONS_INTENT_KEY"

        fun getIntent(context: Context, type: FilterType): Intent {
            val intent = Intent(context, FilterSelectionActivity::class.java)
            intent.putExtra(FILTER_TYPE_ID, type as Parcelable)
            return intent
        }
    }
}
