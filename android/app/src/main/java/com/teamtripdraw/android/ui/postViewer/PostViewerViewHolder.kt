package com.teamtripdraw.android.ui.postViewer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teamtripdraw.android.databinding.ItemPostViewerBinding
import com.teamtripdraw.android.ui.model.UiPostItem

class PostViewerViewHolder(
    private val binding: ItemPostViewerBinding,
    onPostClick: (postId: Long) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.onPostClick = onPostClick
    }

    fun bind(item: UiPostItem) {
        binding.postItem = item
    }

    companion object {
        fun of(
            parent: ViewGroup,
            onPostClick: (postId: Long) -> Unit,
        ): PostViewerViewHolder {
            val binding = ItemPostViewerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
            return PostViewerViewHolder(binding, onPostClick)
        }
    }
}