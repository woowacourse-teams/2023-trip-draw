package com.teamtripdraw.android.ui.allTrips

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.teamtripdraw.android.databinding.ItemLoadingBinding
import com.teamtripdraw.android.ui.model.allTrips.UiAllTripItem
import com.teamtripdraw.android.ui.model.allTrips.UiTripOfAll

class AllTripsAdapter(
    private val viewModel: AllTripsViewModel,
) : ListAdapter<UiAllTripItem, RecyclerView.ViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ALL_TRIP_ITEM_VIEW -> AllTripsViewHolder.of(parent, viewModel)
            else -> LoadingViewHolder.of(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is AllTripsViewHolder) holder.bind(getItem(position) as UiTripOfAll)
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is UiTripOfAll -> ALL_TRIP_ITEM_VIEW
            else -> LOADING_VIEW
        }
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

    class LoadingViewHolder(val binding: ItemLoadingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun of(parent: ViewGroup): LoadingViewHolder =
                LoadingViewHolder(
                    ItemLoadingBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false,
                    ),
                )
        }
    }
}
