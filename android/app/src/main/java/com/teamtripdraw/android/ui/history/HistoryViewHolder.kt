package com.teamtripdraw.android.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teamtripdraw.android.databinding.ItemTripHistoryBinding
import com.teamtripdraw.android.ui.model.UiHistory

class HistoryViewHolder(
    private val binding: ItemTripHistoryBinding,
    viewModel: HistoryViewModel
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.historyViewModel = viewModel
    }

    fun bind(item: UiHistory) {
        binding.historyItem = item
    }

    companion object {
        fun of(
            parent: ViewGroup,
            viewModel: HistoryViewModel,
        ): HistoryViewHolder {
            val binding = ItemTripHistoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
            return HistoryViewHolder(binding, viewModel)
        }
    }
}
