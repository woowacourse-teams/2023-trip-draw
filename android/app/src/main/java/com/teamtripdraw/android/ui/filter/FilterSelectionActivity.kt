package com.teamtripdraw.android.ui.filter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.result.contract.ActivityResultContracts
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
import com.teamtripdraw.android.support.framework.presentation.event.EventObserver
import com.teamtripdraw.android.support.framework.presentation.getParcelableExtraCompat
import com.teamtripdraw.android.ui.filter.address.AddressSelectionActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.typeOf

@AndroidEntryPoint
class FilterSelectionActivity : AppCompatActivity() {

    lateinit var binding: ActivityFilterSelectionBinding
    private val viewModel: FilterSelectionViewModel by viewModels()
    private lateinit var filterViews: List<FilterView>
    private val getSelectedAddress =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode != RESULT_OK) return@registerForActivityResult
            val intent: Intent = result.data!!
            val address = intent.getStringExtra(AddressSelectionActivity.INTENT_KEY_ADDRESS) ?: ""
            viewModel.setAddress(address)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_filter_selection)
        bindViewModel()

        viewModel.setupFilterType(initFilterType())
        setupFilterViews()
        initViewBySelectedOptions()
        initObserver()
    }

    private fun bindViewModel() {
        binding.lifecycleOwner = this
        binding.filterSelectionViewModel = viewModel
    }

    private fun initFilterType(): FilterType =
        intent.getParcelableExtraCompat(FILTER_TYPE_ID) ?: throw IllegalStateException()

    private fun initViewBySelectedOptions() {
        val selectedOptions =
            intent.getParcelableExtraCompat<SelectedOptions>(SELECTED_OPTIONS_HISTORY_KEY) ?: return
        viewModel.setSelectedOption(selectedOptions)
        viewModel.setAddress(selectedOptions.address)
    }

    private fun setupFilterViews() {
        filterViews = listOf(
            FilterView(typeOf<OptionYear>(), binding.filterOptionsYear),
            FilterView(typeOf<OptionMonth>(), binding.filterOptionsMonth),
            FilterView(typeOf<OptionDayOfWeek>(), binding.filterOptionsDayOfWeek),
            FilterView(typeOf<OptionAgeRange>(), binding.filterOptionsAgeRange),
            FilterView(typeOf<OptionGender>(), binding.filterOptionsGender),
        )
    }

    private fun initObserver() {
        initOpenAddressSelectionEventObserve()
        initOpenSearchResultEventObserve()
        initRefreshEventObserve()
        initBackPageObserve()
    }

    private fun initOpenAddressSelectionEventObserve() {
        viewModel.openAddressSelectionEvent.observe(
            this,
            this::onAddressSelectionClick,
        )
    }

    private fun onAddressSelectionClick(isClicked: Boolean) {
        if (isClicked) {
            val intent = Intent(this, AddressSelectionActivity::class.java)
            getSelectedAddress.launch(intent)
        }
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
            viewModel.setAddress()
            filterViews.forEach { it.view.clearCheck() }
        }
    }

    private fun getSelectedOptions(): SelectedOptions {
        val selectedOptions = SelectedOptionsBuilder()
        filterViews.forEach {
            selectedOptions.setSelectedOptions(it, it.view.getCheckedChipIds())
        }
        selectedOptions.setAddress(viewModel.address.value ?: "").setHours(getHour())
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

    private fun initBackPageObserve() {
        viewModel.backPageEvent.observe(
            this,
            EventObserver { if (it) finish() },
        )
    }

    companion object {
        private const val FILTER_TYPE_ID = "FILTER_TYPE_ID"
        private const val SELECTED_OPTIONS_HISTORY_KEY = "SELECTED_OPTIONS_HISTORY_KEY"
        const val SELECTED_OPTIONS_INTENT_KEY = "SELECTED_OPTIONS_INTENT_KEY"

        fun getIntent(
            context: Context,
            type: FilterType,
            selectedOptions: SelectedOptions?,
        ): Intent {
            val intent = Intent(context, FilterSelectionActivity::class.java)
            intent.putExtra(FILTER_TYPE_ID, type as Parcelable)
            intent.putExtra(SELECTED_OPTIONS_HISTORY_KEY, selectedOptions)
            return intent
        }
    }
}
