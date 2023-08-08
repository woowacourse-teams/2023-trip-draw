package com.teamtripdraw.android.ui.history.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teamtripdraw.android.databinding.ItemHistoryDetailPostsBinding
import com.teamtripdraw.android.ui.model.UiPostItem

class HistoryDetailViewHolder(
    private val binding: ItemHistoryDetailPostsBinding,
    viewModel: HistoryDetailViewModel
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.historyDetailViewModel = viewModel
    }

    fun bind(item: UiPostItem) {
        binding.postItem = item
    }

    companion object {
        fun of(
            parent: ViewGroup,
            viewModel: HistoryDetailViewModel,
        ): HistoryDetailViewHolder {
            val binding = ItemHistoryDetailPostsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
            return HistoryDetailViewHolder(binding, viewModel)
        }
    }
}
