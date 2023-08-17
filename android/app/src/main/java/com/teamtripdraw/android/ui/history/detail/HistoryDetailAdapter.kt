package com.teamtripdraw.android.ui.history.detail

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.teamtripdraw.android.ui.model.UiPostItem

class HistoryDetailAdapter(
    private val viewModel: HistoryDetailViewModel
) : ListAdapter<UiPostItem, HistoryDetailViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryDetailViewHolder {
        return HistoryDetailViewHolder.of(parent, viewModel)
    }

    override fun onBindViewHolder(holder: HistoryDetailViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<UiPostItem>() {
            override fun areItemsTheSame(
                oldItem: UiPostItem,
                newItem: UiPostItem,
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: UiPostItem,
                newItem: UiPostItem,
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
