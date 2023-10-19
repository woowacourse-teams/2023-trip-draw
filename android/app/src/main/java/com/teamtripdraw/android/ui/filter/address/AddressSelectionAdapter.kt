package com.teamtripdraw.android.ui.filter.address

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class AddressSelectionAdapter(
    private val selectEvent: (addressNickname: String) -> Unit,
) : ListAdapter<String, AddressSelectionViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressSelectionViewHolder {
        return AddressSelectionViewHolder.of(parent, selectEvent)
    }

    override fun onBindViewHolder(holder: AddressSelectionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<String>() {

            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
                oldItem == newItem
        }
    }
}
