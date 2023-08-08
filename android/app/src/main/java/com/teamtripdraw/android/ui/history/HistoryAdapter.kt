package com.teamtripdraw.android.ui.history

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.teamtripdraw.android.ui.model.UiHistoryItem

class HistoryAdapter(
    private val viewModel: HistoryViewModel
) : ListAdapter<UiHistoryItem, HistoryViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder.of(parent, viewModel)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<UiHistoryItem>() {
            override fun areItemsTheSame(
                oldItem: UiHistoryItem,
                newItem: UiHistoryItem,
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: UiHistoryItem,
                newItem: UiHistoryItem,
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
