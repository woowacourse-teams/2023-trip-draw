package com.teamtripdraw.android.ui.allPosts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.teamtripdraw.android.databinding.ItemLoadingBinding
import com.teamtripdraw.android.ui.model.UiPostOfAll

class AllPostsAdapter(
    private val openPostEvent: (Long) -> Unit,
) : ListAdapter<UiPostOfAll, RecyclerView.ViewHolder>(diffUtil) {

    private lateinit var loadingBinding: ItemLoadingBinding

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).infiniteLoading == "loading") {
            LOADING_VIEW
        } else {
            POSTS_VIEW
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            LOADING_VIEW -> {
                loadingBinding = ItemLoadingBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                )
                LoadingViewHolder(loadingBinding)
            }
            else -> AllPostsViewHolder.of(parent, openPostEvent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is AllPostsViewHolder) holder.bind(getItem(position))
    }

    companion object {
        private const val LOADING_VIEW = 1
        private const val POSTS_VIEW = 2

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

    class LoadingViewHolder(val binding: ItemLoadingBinding) :
        RecyclerView.ViewHolder(binding.root)
}
