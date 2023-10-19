package com.teamtripdraw.android.ui.filter.address

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teamtripdraw.android.databinding.ItemFilterOptionAddressBinding
import com.teamtripdraw.android.ui.model.UiAddressSelectionItem

class AddressSelectionViewHolder(
    val binding: ItemFilterOptionAddressBinding,
    selectEvent: (addressName: UiAddressSelectionItem) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.selectEvent = selectEvent
    }

    fun bind(address: UiAddressSelectionItem) {
        binding.addressItem = address
        binding.tvFilterOptionAddressName.text = address.addressName
    }

    companion object {
        fun of(
            parent: ViewGroup,
            selectEvent: (addressName: UiAddressSelectionItem) -> Unit,
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
