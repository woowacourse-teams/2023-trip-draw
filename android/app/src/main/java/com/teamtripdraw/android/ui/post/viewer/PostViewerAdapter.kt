package com.teamtripdraw.android.ui.post.viewer

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.teamtripdraw.android.ui.model.UiPostItem

class PostViewerAdapter(
    private val viewModel: PostViewerViewModel
) : ListAdapter<UiPostItem, PostViewerViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewerViewHolder {
        return PostViewerViewHolder.of(parent, viewModel)
    }

    override fun onBindViewHolder(holder: PostViewerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<UiPostItem>() {
            override fun areItemsTheSame(
                oldItem: UiPostItem,
                newItem: UiPostItem,
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: UiPostItem,
                newItem: UiPostItem,
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
