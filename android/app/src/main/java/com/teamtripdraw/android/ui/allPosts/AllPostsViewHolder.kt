package com.teamtripdraw.android.ui.allPosts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teamtripdraw.android.databinding.ItemAllPostsBinding
import com.teamtripdraw.android.ui.model.UiPostOfAll

class AllPostsViewHolder(
    private val binding: ItemAllPostsBinding,
    viewModel: AllPostsViewModel,
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.allPostsViewModel = viewModel
    }

    fun bind(item: UiPostOfAll) {
        binding.postItem = item
    }

    companion object {
        fun of(
            parent: ViewGroup,
            viewModel: AllPostsViewModel,
        ): AllPostsViewHolder {
            val binding = ItemAllPostsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
            return AllPostsViewHolder(binding, viewModel)
        }
    }
}
