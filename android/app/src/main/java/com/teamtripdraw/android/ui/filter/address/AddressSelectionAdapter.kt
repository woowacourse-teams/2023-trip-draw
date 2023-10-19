package com.teamtripdraw.android.ui.filter.address

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.teamtripdraw.android.ui.model.UiAddressSelectionItem

class AddressSelectionAdapter(
    private val selectEvent: (address: UiAddressSelectionItem) -> Unit,
) : ListAdapter<UiAddressSelectionItem, AddressSelectionViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressSelectionViewHolder {
        return AddressSelectionViewHolder.of(parent, selectEvent)
    }

    override fun onBindViewHolder(holder: AddressSelectionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<UiAddressSelectionItem>() {

            override fun areItemsTheSame(
                oldItem: UiAddressSelectionItem,
                newItem: UiAddressSelectionItem,
            ): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(
                oldItem: UiAddressSelectionItem,
                newItem: UiAddressSelectionItem,
            ): Boolean =
                oldItem.isSelected == newItem.isSelected &&
                    oldItem.addressName == newItem.addressName
        }
    }
}
