package com.teamtripdraw.android.ui.filter

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.teamtripdraw.android.R
import com.teamtripdraw.android.databinding.ViewFilterOptionsBinding
import com.teamtripdraw.android.domain.model.filterOption.FilterOption
import com.teamtripdraw.android.ui.model.mapper.toText

class FilterOptionsView @JvmOverloads constructor(context: Context, attr: AttributeSet? = null) :
    ConstraintLayout(context, attr) {

    private val chips: MutableList<Chip> = mutableListOf()
    private val binding: ViewFilterOptionsBinding =
        ViewFilterOptionsBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        attr?.let {
            val filterOptionsTitle =
                context.obtainStyledAttributes(it, R.styleable.FilterOptionsView)
            setTitleAppearance(filterOptionsTitle)
            filterOptionsTitle.recycle()
        }
    }

    private fun setTitleAppearance(filterOptionsTitle: TypedArray) {
        when (filterOptionsTitle.getInt(R.styleable.FilterOptionsView_filterOptionsTitle, 1)) {
            TEXT_APPEARANCE_TITLE -> binding.tvFilterOptionsTitle.setTextAppearance(R.style.subtitle_1)
            TEXT_APPEARANCE_SUBTITLE -> binding.tvFilterOptionsTitle.setTextAppearance(R.style.body_1)
        }
    }

    fun setupOptions(options: List<FilterOption>) {
        options.forEach { option -> createOptionChip(option) }
    }

    private fun createOptionChip(option: FilterOption) {
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
        binding.chipGroupFilterOptions.addView(chip)
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

    fun setTitle(title: String) {
        binding.tvFilterOptionsTitle.text = title
    }

    fun getCheckedChipIds(): List<Int> = binding.chipGroupFilterOptions.checkedChipIds

    fun clearCheck() = binding.chipGroupFilterOptions.clearCheck()

    companion object {
        private const val TEXT_APPEARANCE_TITLE = 1
        private const val TEXT_APPEARANCE_SUBTITLE = 2
    }
}
