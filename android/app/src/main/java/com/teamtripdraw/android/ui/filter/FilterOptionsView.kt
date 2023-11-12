package com.teamtripdraw.android.ui.filter

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.chip.ChipGroup
import com.teamtripdraw.android.R
import com.teamtripdraw.android.domain.model.filterOption.FilterOption
import com.teamtripdraw.android.ui.model.mapper.toText

class FilterOptionsView @JvmOverloads constructor(context: Context, attr: AttributeSet? = null) :
    ConstraintLayout(context, attr) {

    private val chips: MutableList<Chip> = mutableListOf()

    private lateinit var titleView: TextView
    private lateinit var chipGroup: ChipGroup

    init {
        initView(context)
        attr?.let {
            val filterOptionsTitle =
                context.obtainStyledAttributes(it, R.styleable.FilterOptionsView)
            setTitleAppearance(filterOptionsTitle)
            filterOptionsTitle.recycle()
        }
    }

    private fun initView(context: Context) {
        val inflateService = Context.LAYOUT_INFLATER_SERVICE
        val layoutInflater = context.getSystemService(inflateService) as LayoutInflater
        layoutInflater.inflate(R.layout.view_filter_options, this, true)
        titleView = findViewById(R.id.tv_filter_options_title)
        chipGroup = findViewById(R.id.chip_group_filter_options)
    }

    private fun setTitleAppearance(filterOptionsTitle: TypedArray) {
        when (
            filterOptionsTitle.getInt(
                R.styleable.FilterOptionsView_filterOptionsTitleAppearance,
                1,
            )
        ) {
            TEXT_APPEARANCE_TITLE -> titleView.setTextAppearance(R.style.subtitle_1)
            TEXT_APPEARANCE_SUBTITLE -> titleView.setTextAppearance(R.style.body_1)
        }
    }

    fun <T> setOptions(options: List<FilterOption<T>>, selectedOptions: List<Int>?) {
        options.forEach { option -> createOptionChip(option) }
        if (selectedOptions != null) checkSelectedOptions(selectedOptions)
    }

    private fun <T> createOptionChip(option: FilterOption<T>) {
        val chip = Chip(context)
        chip.id = option.id
        chip.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
        )
        settingChipStyle(chip)
        chip.isCheckable = true
        chip.text = option.toText()
        chips.add(chip)
        chipGroup.addView(chip)
    }

    private fun settingChipStyle(chip: Chip) {
        chip.setChipDrawable(
            ChipDrawable.createFromAttributes(
                context,
                null,
                0,
                R.style.filterChip,
            ),
        )
        chip.setTextAppearance(R.style.chipTextAppearance)
    }

    private fun checkSelectedOptions(selectedOptions: List<Int>) {
        chips.forEach {
            if (selectedOptions.contains(it.id)) it.isChecked = true
        }
    }

    fun setTitle(title: String) {
        titleView.text = title
    }

    fun getCheckedChipIds(): List<Int> = chipGroup.checkedChipIds

    fun clearCheck() = chipGroup.clearCheck()

    companion object {
        private const val TEXT_APPEARANCE_TITLE = 1
        private const val TEXT_APPEARANCE_SUBTITLE = 2
    }
}
