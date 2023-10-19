package com.teamtripdraw.android.ui.allPosts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teamtripdraw.android.databinding.ItemAllPostsBinding
import com.teamtripdraw.android.ui.model.allPosts.UiPostOfAll

class AllPostsViewHolder(
    private val binding: ItemAllPostsBinding,
    openPostEvent: (Long) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.openPostEvent = openPostEvent
    }

    fun bind(item: UiPostOfAll) {
        binding.postItem = item
    }

    companion object {
        fun of(
            parent: ViewGroup,
            openPostEvent: (Long) -> Unit,
        ): AllPostsViewHolder {
            val binding = ItemAllPostsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
            return AllPostsViewHolder(binding, openPostEvent)
        }
    }
}
