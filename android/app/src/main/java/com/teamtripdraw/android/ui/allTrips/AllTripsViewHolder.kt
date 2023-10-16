package com.teamtripdraw.android.ui.allTrips

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teamtripdraw.android.databinding.ItemAllTripsBinding
import com.teamtripdraw.android.ui.model.allTrips.UiTripOfAll

class AllTripsViewHolder(
    private val binding: ItemAllTripsBinding,
    viewModel: AllTripsViewModel,
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.allTripsViewModel = viewModel
    }

    fun bind(item: UiTripOfAll) {
        binding.tripItem = item
    }

    companion object {
        fun of(
            parent: ViewGroup,
            viewModel: AllTripsViewModel,
        ): AllTripsViewHolder {
            val binding = ItemAllTripsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
            return AllTripsViewHolder(binding, viewModel)
        }
    }
}
