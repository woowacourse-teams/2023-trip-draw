package com.teamtripdraw.android.ui.history

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.teamtripdraw.android.ui.model.UiPreviewTrip

class HistoryAdapter(
    private val viewModel: HistoryViewModel,
) : ListAdapter<UiPreviewTrip, HistoryViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder.of(parent, viewModel)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<UiPreviewTrip>() {
            override fun areItemsTheSame(
                oldItem: UiPreviewTrip,
                newItem: UiPreviewTrip,
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: UiPreviewTrip,
                newItem: UiPreviewTrip,
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
