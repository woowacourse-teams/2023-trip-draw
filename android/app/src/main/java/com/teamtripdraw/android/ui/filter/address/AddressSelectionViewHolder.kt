package com.teamtripdraw.android.ui.filter.address

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teamtripdraw.android.databinding.ItemFilterOptionAddressBinding

class AddressSelectionViewHolder(
    private val binding: ItemFilterOptionAddressBinding,
    selectEvent: (addressName: String) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.selectEvent = selectEvent
    }

    fun bind(addressName: String) {
        binding.addressName = addressName
        binding.tvFilterOptionAddressName.text = addressName
    }

    companion object {
        fun of(
            parent: ViewGroup,
            selectEvent: (addressName: String) -> Unit,
        ): AddressSelectionViewHolder {
            val binding = ItemFilterOptionAddressBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
            return AddressSelectionViewHolder(binding, selectEvent)
        }
    }
}
