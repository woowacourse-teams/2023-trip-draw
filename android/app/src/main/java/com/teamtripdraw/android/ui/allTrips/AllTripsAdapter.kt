package com.teamtripdraw.android.ui.allTrips

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.teamtripdraw.android.ui.model.allTrips.UiAllTripItem
import com.teamtripdraw.android.ui.model.allTrips.UiTripOfAll

class AllTripsAdapter(
    private val viewModel: AllTripsViewModel,
) : ListAdapter<UiAllTripItem, AllTripsViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllTripsViewHolder {
        return AllTripsViewHolder.of(parent, viewModel)
    }

    override fun onBindViewHolder(holder: AllTripsViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is UiTripOfAll -> holder.bind(item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return ALL_TRIP_ITEM_VIEW
    }

    companion object {
        private const val LOADING_VIEW = 1
        private const val ALL_TRIP_ITEM_VIEW = 2

        val diffUtil = object : DiffUtil.ItemCallback<UiAllTripItem>() {
            override fun areItemsTheSame(
                oldItem: UiAllTripItem,
                newItem: UiAllTripItem,
            ): Boolean {
                if (oldItem !is UiTripOfAll || newItem !is UiTripOfAll) return false
                return oldItem.tripId == newItem.tripId
            }

            override fun areContentsTheSame(
                oldItem: UiAllTripItem,
                newItem: UiAllTripItem,
            ): Boolean {
                if (oldItem !is UiTripOfAll || newItem !is UiTripOfAll) return false
                return oldItem == newItem
            }
        }
    }
}
