package com.teamtripdraw.android.ui.post.viewer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teamtripdraw.android.databinding.ItemPostViewerBinding
import com.teamtripdraw.android.ui.model.UiPostItem

class PostViewerViewHolder(
    private val binding: ItemPostViewerBinding,
    viewModel: PostViewerViewModel
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.postViewerViewModel = viewModel
    }

    fun bind(item: UiPostItem) {
        binding.postItem = item
    }

    companion object {
        fun of(
            parent: ViewGroup,
            viewModel: PostViewerViewModel,
        ): PostViewerViewHolder {
            val binding = ItemPostViewerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
            return PostViewerViewHolder(binding, viewModel)
        }
    }
}
