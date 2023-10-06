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
import com.teamtripdraw.android.domain.model.filterOption.OptionAgeRange
import com.teamtripdraw.android.domain.model.filterOption.OptionDayOfWeek
import com.teamtripdraw.android.domain.model.filterOption.OptionGender
import com.teamtripdraw.android.domain.model.filterOption.OptionHour
import com.teamtripdraw.android.domain.model.filterOption.OptionMonth
import com.teamtripdraw.android.domain.model.filterOption.OptionYear
import com.teamtripdraw.android.support.framework.presentation.getParcelableExtraCompat
import kotlin.reflect.typeOf

class FilterSelectionActivity : AppCompatActivity() {

    lateinit var binding: ActivityFilterSelectionBinding
    private val viewModel: FilterSelectionViewModel by viewModels()
    private lateinit var filterViews: List<FilterView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_filter_selection)
        bindViewModel()

        viewModel.setupFilterType(initFilterType())
        filterViews = setupFilterViews()
        initObserver()
    }

    private fun bindViewModel() {
        binding.lifecycleOwner = this
        binding.filterSelectionViewModel = viewModel
    }

    private fun initFilterType(): FilterType =
        intent.getParcelableExtraCompat(FILTER_TYPE_ID) ?: throw IllegalStateException()

    private fun setupFilterViews() = listOf(
        FilterView(typeOf<OptionYear>(), binding.filterOptionsYear),
        FilterView(typeOf<OptionMonth>(), binding.filterOptionsMonth),
        FilterView(typeOf<OptionDayOfWeek>(), binding.filterOptionsDayOfWeek),
        FilterView(typeOf<OptionAgeRange>(), binding.filterOptionsAgeRange),
        FilterView(typeOf<OptionGender>(), binding.filterOptionsGender),
    )

    private fun initObserver() {
        initOpenSearchResultEventObserve()
        initRefreshEventObserve()
    }

    private fun initOpenSearchResultEventObserve() {
        viewModel.openSearchResultEvent.observe(
            this,
            this::onSearchClick,
        )
    }

    private fun onSearchClick(isClicked: Boolean) {
        if (isClicked) {
            val resultIntent = Intent()
            resultIntent.putExtra(SELECTED_OPTIONS_INTENT_KEY, getSelectedOptions())
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }

    private fun initRefreshEventObserve() {
        viewModel.refreshEvent.observe(
            this,
            this::onRefreshClick,
        )
    }

    private fun onRefreshClick(isClicked: Boolean) {
        if (isClicked) {
            filterViews.forEach { it.view.clearCheck() }
            binding.filterOptionsHourFrom.value = binding.filterOptionsHourFrom.minValue
            binding.filterOptionsHourTo.value = binding.filterOptionsHourFrom.maxValue
        }
    }

    private fun getSelectedOptions(): SelectedOptions {
        val selectedOptions = SelectedOptionsBuilder()
        filterViews.forEach {
            selectedOptions.setSelectedOptions(it, it.view.getCheckedChipIds())
        }
        selectedOptions.setAddress("임시 주소입니다").setHours(getHour())
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
