package com.teamtripdraw.android.ui.allTrips

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.teamtripdraw.android.ui.model.UiTripOfAll

class AllTripsAdapter(
    private val viewModel: AllTripsViewModel,
) : ListAdapter<UiTripOfAll, AllTripsViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllTripsViewHolder {
        return AllTripsViewHolder.of(parent, viewModel)
    }

    override fun onBindViewHolder(holder: AllTripsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<UiTripOfAll>() {
            override fun areItemsTheSame(
                oldItem: UiTripOfAll,
                newItem: UiTripOfAll,
            ): Boolean {
                return oldItem.tripId == newItem.tripId
            }

            override fun areContentsTheSame(
                oldItem: UiTripOfAll,
                newItem: UiTripOfAll,
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
