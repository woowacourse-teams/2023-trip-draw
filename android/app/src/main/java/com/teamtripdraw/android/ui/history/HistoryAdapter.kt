package com.teamtripdraw.android.ui.history

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.teamtripdraw.android.ui.model.UiTrip

class HistoryAdapter(
    private val viewModel: HistoryViewModel
) : ListAdapter<UiTrip, HistoryViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder.of(parent, viewModel)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<UiTrip>() {
            override fun areItemsTheSame(
                oldItem: UiTrip,
                newItem: UiTrip,
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: UiTrip,
                newItem: UiTrip,
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
