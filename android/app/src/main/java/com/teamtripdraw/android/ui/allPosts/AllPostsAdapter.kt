package com.teamtripdraw.android.ui.allPosts

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.teamtripdraw.android.ui.model.UiPostOfAll

class AllPostsAdapter(
    private val viewModel: AllPostsViewModel,
) : ListAdapter<UiPostOfAll, AllPostsViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllPostsViewHolder {
        return AllPostsViewHolder.of(parent, viewModel)
    }

    override fun onBindViewHolder(holder: AllPostsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<UiPostOfAll>() {
            override fun areItemsTheSame(
                oldItem: UiPostOfAll,
                newItem: UiPostOfAll,
            ): Boolean {
                return oldItem.postId == newItem.postId
            }

            override fun areContentsTheSame(
                oldItem: UiPostOfAll,
                newItem: UiPostOfAll,
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
