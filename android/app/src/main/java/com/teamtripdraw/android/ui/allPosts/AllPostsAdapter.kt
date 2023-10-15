package com.teamtripdraw.android.ui.allPosts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.teamtripdraw.android.databinding.ItemLoadingBinding
import com.teamtripdraw.android.ui.model.allPosts.UiItemView
import com.teamtripdraw.android.ui.model.allPosts.UiLoadingItem
import com.teamtripdraw.android.ui.model.allPosts.UiPostOfAll

class AllPostsAdapter(
    private val openPostEvent: (Long) -> Unit,
) : ListAdapter<UiItemView, RecyclerView.ViewHolder>(diffUtil) {

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position) is UiLoadingItem) {
            LOADING_VIEW
        } else {
            POST_VIEW
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            LOADING_VIEW -> LoadingViewHolder.of(parent)
            else -> AllPostsViewHolder.of(parent, openPostEvent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is AllPostsViewHolder) holder.bind(getItem(position) as UiPostOfAll)
    }

    companion object {
        private const val LOADING_VIEW = 1
        private const val POST_VIEW = 2

        val diffUtil = object : DiffUtil.ItemCallback<UiItemView>() {
            override fun areItemsTheSame(
                oldItem: UiItemView,
                newItem: UiItemView,
            ): Boolean {
                if (oldItem is UiPostOfAll && newItem is UiPostOfAll) {
                    return oldItem.postId == newItem.postId
                }
                return false
            }

            override fun areContentsTheSame(
                oldItem: UiItemView,
                newItem: UiItemView,
            ): Boolean {
                if (oldItem is UiPostOfAll && newItem is UiPostOfAll) {
                    return oldItem == newItem
                }
                return false
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
