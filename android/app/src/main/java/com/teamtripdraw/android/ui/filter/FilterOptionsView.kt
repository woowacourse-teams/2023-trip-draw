package com.teamtripdraw.android.ui.filter

import android.content.Context
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

            when (filterOptionsTitle.getInt(R.styleable.FilterOptionsView_filterOptionsTitle, 1)) {
                TEXT_APPEARANCE_TITLE -> binding.tvFilterOptionsTitle.setTextAppearance(R.style.subtitle_1)
                TEXT_APPEARANCE_SUBTITLE -> binding.tvFilterOptionsTitle.setTextAppearance(R.style.body_1)
            }
            filterOptionsTitle.recycle()
        }
    }

    fun setupOptions(options: List<FilterOption>) {
        createOptionChips(options)
    }

    fun setTitle(title: String) {
        binding.tvFilterOptionsTitle.text = title
    }

    private fun createOptionChips(options: List<FilterOption>) {
        options.forEach {
            val chip = Chip(context)
            chips.add(chip)
            chip.id = it.id
            chip.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
            )

            val chipDrawable = ChipDrawable.createFromAttributes(
                context,
                null,
                0,
                R.style.filterChip,
            )
            chip.setChipDrawable(chipDrawable)
            chip.setTextAppearance(R.style.chipTextAppearance)

            chip.isCheckable = true
            chip.text = it.toText()
            binding.chipGroupFilterOptions.addView(chip)
        }
    }

    fun getCheckedChipIds(): List<Int> = binding.chipGroupFilterOptions.checkedChipIds

    fun clearCheck() = binding.chipGroupFilterOptions.clearCheck()

    companion object {
        private const val TEXT_APPEARANCE_TITLE = 1
        private const val TEXT_APPEARANCE_SUBTITLE = 2
    }
}
